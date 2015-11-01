/*
 * JSONUtil.java
 *
 * Copyright (c) 2015 ITSTAKE
 *
 * This program is free software: you can redistribute it and/or modify
 *
 * it under the terms of the GNU General Public License as published by
 *
 * the Free Software Foundation, either version 3 of the License, or
 *
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package tk.itstake.util;

/**
 * Created by bexco on 2015-07-26.
 */
public class JSONUtil {

    public static String getPretty(String jsonString) {

        final String INDENT = "    ";
        StringBuffer prettyJsonSb = new StringBuffer();

        int indentDepth = 0;
        String targetString = null;
        boolean isString = false;
        for(int i=0; i<jsonString.length(); i++) {
            targetString = jsonString.substring(i, i+1);
            if(targetString.equals("{")||targetString.equals("[")) {
                if(!isString) {
                    prettyJsonSb.append(targetString).append("\n");
                    indentDepth++;
                    for (int j = 0; j < indentDepth; j++) {
                        prettyJsonSb.append(INDENT);
                    }
                } else {
                    prettyJsonSb.append(targetString);
                }
            }
            else if(targetString.equals("}")||targetString.equals("]")) {
                if(!isString) {
                    prettyJsonSb.append("\n");
                    indentDepth--;
                    for (int j = 0; j < indentDepth; j++) {
                        prettyJsonSb.append(INDENT);
                    }
                    prettyJsonSb.append(targetString);
                } else {
                    prettyJsonSb.append(targetString);
                }
            }
            else if(targetString.equals(",")) {
                if(!isString) {
                    prettyJsonSb.append(targetString);
                    prettyJsonSb.append("\n");
                    for (int j = 0; j < indentDepth; j++) {
                        prettyJsonSb.append(INDENT);
                    }
                } else {
                    prettyJsonSb.append(targetString);
                }
            } else if(targetString.equals("\"")) {
                isString = !isString;
                prettyJsonSb.append(targetString);
            }
            else {
                prettyJsonSb.append(targetString);
            }

        }


        return prettyJsonSb.toString();

    }
}
