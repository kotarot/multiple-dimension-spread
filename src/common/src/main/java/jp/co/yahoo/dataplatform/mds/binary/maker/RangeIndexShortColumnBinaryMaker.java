/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.yahoo.dataplatform.mds.binary.maker;

import java.io.IOException;
import java.nio.ByteBuffer;

import java.util.Map;
import java.util.HashMap;

import jp.co.yahoo.dataplatform.mds.constants.PrimitiveByteLength;
import jp.co.yahoo.dataplatform.mds.spread.column.ICell;
import jp.co.yahoo.dataplatform.mds.spread.column.IColumn;
import jp.co.yahoo.dataplatform.mds.spread.column.PrimitiveCell;
import jp.co.yahoo.dataplatform.mds.spread.column.ColumnType;
import jp.co.yahoo.dataplatform.mds.binary.ColumnBinary;
import jp.co.yahoo.dataplatform.mds.binary.ColumnBinaryMakerConfig;
import jp.co.yahoo.dataplatform.mds.binary.ColumnBinaryMakerCustomConfigNode;
import jp.co.yahoo.dataplatform.mds.binary.maker.index.RangeShortIndex;
import jp.co.yahoo.dataplatform.mds.blockindex.BlockIndexNode;
import jp.co.yahoo.dataplatform.mds.blockindex.ShortRangeBlockIndex;
import jp.co.yahoo.dataplatform.mds.inmemory.IMemoryAllocator;

public class RangeIndexShortColumnBinaryMaker extends UniqShortColumnBinaryMaker{

  @Override
  public ColumnBinary toBinary(final ColumnBinaryMakerConfig commonConfig , final ColumnBinaryMakerCustomConfigNode currentConfigNode , final IColumn column , final MakerCache makerCache ) throws IOException{
    ColumnBinaryMakerConfig currentConfig = commonConfig;
    if( currentConfigNode != null ){
      currentConfig = currentConfigNode.getCurrentConfig();
    }
    Map<Short,Integer> dicMap = new HashMap<Short,Integer>();
    int columnIndexLength = column.size() * PrimitiveByteLength.INT_LENGTH;
    int dicBufferSize = ( column.size() + 1 ) * PrimitiveByteLength.SHORT_LENGTH;
    byte[] binaryRaw = new byte[ ( PrimitiveByteLength.INT_LENGTH * 2 ) + columnIndexLength + dicBufferSize ];
    ByteBuffer indexWrapBuffer = ByteBuffer.wrap( binaryRaw , 0 , PrimitiveByteLength.INT_LENGTH + columnIndexLength );
    ByteBuffer dicLengthBuffer = ByteBuffer.wrap( binaryRaw , PrimitiveByteLength.INT_LENGTH + columnIndexLength , PrimitiveByteLength.INT_LENGTH );
    ByteBuffer dicWrapBuffer = ByteBuffer.wrap( binaryRaw , PrimitiveByteLength.INT_LENGTH * 2 + columnIndexLength , dicBufferSize );
    indexWrapBuffer.putInt( columnIndexLength );

    dicMap.put( null , Integer.valueOf(0) );
    dicWrapBuffer.putShort( Short.valueOf( (short)0 ) );

    Short min = Short.MAX_VALUE;
    Short max = Short.MIN_VALUE;
    int rowCount = 0;
    for( int i = 0 ; i < column.size() ; i++ ){
      ICell cell = column.get(i);
      Short target = null;
      if( cell.getType() != ColumnType.NULL ){
        rowCount++;
        PrimitiveCell stringCell = (PrimitiveCell) cell;
        target = Short.valueOf( stringCell.getRow().getShort() );
      }
      if( ! dicMap.containsKey( target ) ){
        if( 0 < min.compareTo( target ) ){
          min = Short.valueOf( target );
        }
        if( max.compareTo( target ) < 0 ){
          max = Short.valueOf( target );
        }
        dicMap.put( target , dicMap.size() );
        dicWrapBuffer.putShort( target.shortValue() );
      }
      indexWrapBuffer.putInt( dicMap.get( target ) );
    }

    int dicLength = dicMap.size() * PrimitiveByteLength.SHORT_LENGTH;
    int dataLength = binaryRaw.length - ( dicBufferSize - dicLength );
    dicLengthBuffer.putInt( dicLength );
    byte[] compressBinary = currentConfig.compressorClass.compress( binaryRaw , 0 , dataLength );

    byte[] binary = new byte[ PrimitiveByteLength.SHORT_LENGTH * 2 + compressBinary.length ];
    ByteBuffer wrapBuffer = ByteBuffer.wrap( binary , 0 , binary.length );
    wrapBuffer.putShort( min );
    wrapBuffer.putShort( max );
    wrapBuffer.put( compressBinary );

    return new ColumnBinary( this.getClass().getName() , currentConfig.compressorClass.getClass().getName() , column.getColumnName() , ColumnType.SHORT , rowCount , dataLength , rowCount * PrimitiveByteLength.SHORT_LENGTH , dicMap.size() , binary , 0 , binary.length , null );
  }

  @Override
  public IColumn toColumn( final ColumnBinary columnBinary , final IPrimitiveObjectConnector primitiveObjectConnector ) throws IOException{
    ByteBuffer wrapBuffer = ByteBuffer.wrap( columnBinary.binary , columnBinary.binaryStart , columnBinary.binaryLength );
    Short min = Short.valueOf( wrapBuffer.getShort() );
    Short max = Short.valueOf( wrapBuffer.getShort() );
    return new HeaderIndexLazyColumn(
      columnBinary.columnName ,
      columnBinary.columnType ,
      new ShortColumnManager(
        columnBinary ,
        primitiveObjectConnector ,
        columnBinary.binaryStart + ( PrimitiveByteLength.SHORT_LENGTH * 2 ) ,
        columnBinary.binaryLength - ( PrimitiveByteLength.SHORT_LENGTH * 2 )
      )
      , new RangeShortIndex( min , max )
    );
  }

  @Override
  public void loadInMemoryStorage( final ColumnBinary columnBinary , final IMemoryAllocator allocator ) throws IOException{
    loadInMemoryStorage( columnBinary , allocator , columnBinary.binaryStart + ( PrimitiveByteLength.SHORT_LENGTH * 2 ) , columnBinary.binaryLength - ( PrimitiveByteLength.SHORT_LENGTH * 2 ) );
  }

  @Override
  public void setBlockIndexNode( final BlockIndexNode parentNode , final ColumnBinary columnBinary ) throws IOException{
    ByteBuffer wrapBuffer = ByteBuffer.wrap( columnBinary.binary , columnBinary.binaryStart , columnBinary.binaryLength );
    Short min = Short.valueOf( wrapBuffer.getShort() );
    Short max = Short.valueOf( wrapBuffer.getShort() );
    BlockIndexNode currentNode = parentNode.getChildNode( columnBinary.columnName );
    currentNode.setBlockIndex( new ShortRangeBlockIndex( min , max ) );
  }

}
