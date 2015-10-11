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
