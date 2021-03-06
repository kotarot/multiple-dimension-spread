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
package jp.co.yahoo.dataplatform.mds.spread.column;

import java.io.IOException;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.apache.arrow.vector.BitVector;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.memory.BufferAllocator;

import jp.co.yahoo.dataplatform.schema.objects.PrimitiveObject;

public class TestArrowBooleanConnector{

  @Test
  public void T_convert_1() throws IOException{
    BufferAllocator allocator = new RootAllocator( 1024 * 1024 * 10 );
    BitVector vector = new BitVector( "test" , allocator );
    vector.allocateNew();
    vector.setSafe( 0 , 0 );  
    vector.setSafe( 1 , 1 );  
    vector.setSafe( 2 , 0 );  
    vector.setNull( 3 );  
    vector.setSafe( 4 , 1 );  
    vector.setSafe( 5 , 1 );  
    vector.setSafe( 6 , 1 );  
    vector.setNull( 7 );  
    vector.setValueCount( 8 );

    IColumn column = ArrowColumnFactory.convert( "test" , vector );
    assertEquals( column.getColumnName() , "test" );
    assertEquals( column.size() , 8 );
    assertTrue( ( column.getColumnType() == ColumnType.BOOLEAN ) );
    assertEquals( ( (PrimitiveObject)( column.get(0).getRow() ) ).getBoolean() , false  );
    assertEquals( ( (PrimitiveObject)( column.get(1).getRow() ) ).getBoolean() , true  );
    assertEquals( ( (PrimitiveObject)( column.get(2).getRow() ) ).getBoolean() , false  );
    assertEquals( column.get(3).getRow() , null  );
    assertEquals( ( (PrimitiveObject)( column.get(4).getRow() ) ).getBoolean() , true  );
    assertEquals( ( (PrimitiveObject)( column.get(5).getRow() ) ).getBoolean() , true  );
    assertEquals( ( (PrimitiveObject)( column.get(6).getRow() ) ).getBoolean() , true  );
    assertEquals( column.get(7).getRow() , null  );
  }

}
