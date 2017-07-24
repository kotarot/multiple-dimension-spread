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
package jp.co.yahoo.dataplatform.mds.spread.column.filter;

public class LeStringCompareFilter implements IStringCompareFilter{

  private final String str;

  public LeStringCompareFilter( final String str ){
    this.str = str;
  }

  @Override
  public IStringComparator getStringComparator(){
    return new LeStringComparator( str );
  }

  @Override
  public StringCompareFilterType getStringCompareFilterType(){
    return StringCompareFilterType.LE;
  }

  @Override
  public FilterType getFilterType(){
    return FilterType.STRING_COMPARE;
  }

  private class LeStringComparator implements IStringComparator{

    private final String str;

    public LeStringComparator( final String str ){
      this.str = str;
    }

    @Override
    public boolean isFilterString( final String target ){
      return str.compareTo( target ) <= 0;
    }

  }

}
