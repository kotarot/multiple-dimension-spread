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
package jp.co.yahoo.dataplatform.mds.binary;

import jp.co.yahoo.dataplatform.mds.util.Pair;
import jp.co.yahoo.dataplatform.mds.binary.maker.*;

public final class ColumnBinaryMakerNameShortCut{

  private static final Pair CLASS_NAME_PAIR = new Pair();

  static{
    CLASS_NAME_PAIR.set( DumpArrayColumnBinaryMaker.class.getName()   , "D0" );
    CLASS_NAME_PAIR.set( DumpBooleanColumnBinaryMaker.class.getName() , "D1" );
    CLASS_NAME_PAIR.set( DumpBytesColumnBinaryMaker.class.getName()   , "D3" );
    CLASS_NAME_PAIR.set( DumpDoubleColumnBinaryMaker.class.getName()  , "D4" );
    CLASS_NAME_PAIR.set( DumpFloatColumnBinaryMaker.class.getName()   , "D5" );
    CLASS_NAME_PAIR.set( DumpSpreadColumnBinaryMaker.class.getName()  , "D9" );
    CLASS_NAME_PAIR.set( DumpUnionColumnBinaryMaker.class.getName()   , "D11" );

    CLASS_NAME_PAIR.set( RangeDumpDoubleColumnBinaryMaker.class.getName()  , "RD0" );
    CLASS_NAME_PAIR.set( RangeDumpFloatColumnBinaryMaker.class.getName()   , "RD5" );
    // Mistake
    CLASS_NAME_PAIR.set( OptimizeLongColumnBinaryMaker.class.getName()    , "OD0" );
    CLASS_NAME_PAIR.set( OptimizeLongColumnBinaryMaker.class.getName()    , "O0" );
    CLASS_NAME_PAIR.set( OptimizeFloatColumnBinaryMaker.class.getName()   , "O1" );
    CLASS_NAME_PAIR.set( OptimizeDoubleColumnBinaryMaker.class.getName()  , "O2" );
    CLASS_NAME_PAIR.set( OptimizeStringColumnBinaryMaker.class.getName()  , "O11" );

    CLASS_NAME_PAIR.set( OptimizeDumpLongColumnBinaryMaker.class.getName()    , "OD10" );
    CLASS_NAME_PAIR.set( OptimizeDumpStringColumnBinaryMaker.class.getName()  , "OD11" );

    CLASS_NAME_PAIR.set( OptimizeIndexDumpStringColumnBinaryMaker.class.getName()   , "OI11" );

    CLASS_NAME_PAIR.set( UnsafeRangeDumpFloatColumnBinaryMaker.class.getName()  , "XD1" );
    CLASS_NAME_PAIR.set( UnsafeRangeDumpDoubleColumnBinaryMaker.class.getName()  , "XD2" );

    CLASS_NAME_PAIR.set( UnsafeOptimizeLongColumnBinaryMaker.class.getName()    , "XO0" );
    CLASS_NAME_PAIR.set( UnsafeOptimizeFloatColumnBinaryMaker.class.getName()  , "XO1" );
    CLASS_NAME_PAIR.set( UnsafeOptimizeDoubleColumnBinaryMaker.class.getName()  , "XO2" );
    CLASS_NAME_PAIR.set( UnsafeOptimizeStringColumnBinaryMaker.class.getName()  , "XO11" );

    CLASS_NAME_PAIR.set( UnsafeOptimizeDumpLongColumnBinaryMaker.class.getName()    , "XOD10" );
    CLASS_NAME_PAIR.set( UnsafeOptimizeDumpStringColumnBinaryMaker.class.getName()  , "XOD11" );

    CLASS_NAME_PAIR.set( ConstantColumnBinaryMaker.class.getName()   , "C0" );
  }

  private ColumnBinaryMakerNameShortCut(){}

  public static String getShortCutName( final String className ){
    String shortCutName = CLASS_NAME_PAIR.getPair2( className );
    if( shortCutName == null ){
      return className;
    }
    return shortCutName;
  }

  public static String getClassName( final String shortCutName ){
    String className = CLASS_NAME_PAIR.getPair1( shortCutName );
    if( className == null ){
      return shortCutName;
    }
    return className;
  }

}
