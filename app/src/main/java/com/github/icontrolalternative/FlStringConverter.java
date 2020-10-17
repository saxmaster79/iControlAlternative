package com.github.icontrolalternative;

import android.util.Log;

public class FlStringConverter {


    public String hexToString(String hexString) {
        if (hexString == null || hexString.length() < 3) {
            return "";
        }
        String code = hexString.substring(0, 2);
        switch (code) {
            case "FL":
                String src = hexString.substring(4);
                StringBuilder converted = new StringBuilder(src.length() / 2);
                for (int i = 0; (i + 1) < src.length(); i = i + 2) {
                    int read = Integer.parseInt(src.substring(i, i + 2), 16);    // convert the String to hex integer
                    converted.append((char) read);
                }
                return converted.toString();
            case "LM":
                return getPlayingListeningMode(hexString.substring(2));
            default:
                return "";
        }

    }

    private String getPlayingListeningMode(String string) {
        switch (string) {
            case "0001": return "STEREO";
            case "0002": return "F.S.SURR FOCUS";
            case "0003": return "F.S.SURR WIDE";
            case "0004": return "RETRIEVER AIR";
            case "0101": return "[)(]PLIIx MOVIE";
            case "0102": return "[)(]PLII MOVIE";
            case "0103": return "[)(]PLIIx MUSIC";
            case "0104": return "[)(]PLII MUSIC";
            case "0105": return "[)(]PLIIx GAME";
            case "0106": return "[)(]PLII GAME";
            case "0107": return "[)(]PROLOGIC";
            case "0108": return "Neo:6 CINEMA";
            case "0109": return "Neo:6 MUSIC";
            case "0110": return "DVR/BDR";
            case "010a": return "XM HD Surround";
            case "010b": return "NEURAL SURR";
            case "010c": return "2ch Straight Decode";
            case "010d": return "[)(]PLIIz HEIGHT";
            case "010e": return "WIDE SURR MOVIE";
            case "010f": return "WIDE SURR MUSIC";
            case "1101": return "[)(]PLIIx MOVIE";
            case "1102": return "[)(]PLIIx MUSIC";
            case "1103": return "[)(]DIGITAL EX";
            case "1104": return "DTS +Neo:6 / DTS-HD +Neo:6";
            case "1105": return "ES MATRIX";
            case "1106": return "ES DISCRETE";
            case "1107": return "DTS-ES 7.1";
            case "1108": return "multi ch Straight Decode";
            case "1109": return "[)(]PLIIz HEIGHT";
            case "110a": return "WIDE SURR MOVIE";
            case "110b": return "WIDE SURR MUSIC";
            case "0201": return "ACTION";
            case "0202": return "DRAMA";
            case "0203": return "SCI-FI";
            case "0204": return "MONOFILM";
            case "0205": return "ENT.SHOW";
            case "0206": return "EXPANDED";
            case "0207": return "TV SURROUND";
            case "0208": return "ADVANCEDGAME";
            case "0209": return "SPORTS";
            case "020a": return "CLASSICAL";
            case "020b": return "ROCK/POP";
            case "020c": return "UNPLUGGED";
            case "020d": return "EXT.STEREO";
            case "020e": return "PHONES SURR.";
            case "0301": return "[)(]PLIIx MOVIE +THX";
            case "0302": return "[)(]PLII MOVIE +THX";
            case "0303": return "[)(]PL +THX CINEMA";
            case "0304": return "Neo:6 CINEMA +THX";
            case "0305": return "THX CINEMA";
            case "0306": return "[)(]PLIIx MUSIC +THX";
            case "0307": return "[)(]PLII MUSIC +THX";
            case "0308": return "[)(]PL +THX MUSIC";
            case "0309": return "Neo:6 MUSIC +THX";
            case "030a": return "THX MUSIC";
            case "030b": return "[)(]PLIIx GAME +THX";
            case "030c": return "[)(]PLII GAME +THX";
            case "030d": return "[)(]PL +THX GAMES";
            case "030e": return "THX ULTRA2 GAMES";
            case "030f": return "THX SELECT2 GAMES";
            case "0310": return "THX GAMES";
            case "0311": return "[)(]PLIIz +THX CINEMA";
            case "0312": return "[)(]PLIIz +THX MUSIC";
            case "0313": return "[)(]PLIIz +THX GAMES";
            case "1301": return "THX Surr EX";
            case "1302": return "Neo:6 +THX CINEMA";
            case "1303": return "ES MTRX +THX CINEMA";
            case "1304": return "ES DISC +THX CINEMA";
            case "1305": return "ES7.1 +THX CINEMA";
            case "1306": return "[)(]PLIIx MOVIE +THX";
            case "1307": return "THX ULTRA2 CINEMA";
            case "1308": return "THX SELECT2 CINEMA";
            case "1309": return "THX CINEMA";
            case "130a": return "Neo:6 +THX MUSIC";
            case "130b": return "ES MTRX +THX MUSIC";
            case "130c": return "ES DISC +THX MUSIC";
            case "130d": return "ES7.1 +THX MUSIC";
            case "130e": return "[)(]PLIIx MUSIC +THX";
            case "130f": return "THX ULTRA2 MUSIC";
            case "1310": return "THX SELECT2 MUSIC";
            case "1311": return "THX MUSIC";
            case "1312": return "Neo:6 +THX GAMES";
            case "1313": return "ES MTRX +THX GAMES";
            case "1314": return "ES DISC +THX GAMES";
            case "1315": return "ES7.1 +THX GAMES";
            case "1316": return "[)(]EX +THX GAMES";
            case "1317": return "THX ULTRA2 GAMES";
            case "1318": return "THX SELECT2 GAMES";
            case "1319": return "THX GAMES";
            case "131a": return "[)(]PLIIz +THX CINEMA";
            case "131b": return "[)(]PLIIz +THX MUSIC";
            case "131c": return "[)(]PLIIz +THX GAMES";
            case "0401": return "STEREO";
            case "0402": return "[)(]PLII MOVIE";
            case "0403": return "[)(]PLIIx MOVIE";
            case "0404": return "Neo:6 CINEMA";
            case "0405": return "AUTO SURROUND Straight Decode";
            case "0406": return "[)(]DIGITAL EX";
            case "0407": return "[)(]PLIIx MOVIE";
            case "0408": return "DTS +Neo:6";
            case "0409": return "ES MATRIX";
            case "040a": return "ES DISCRETE";
            case "040b": return "DTS-ES 7.1";
            case "040c": return "XM HD Surround";
            case "040d": return "NEURALSURR";
            case "040e": return "RETRIEVER AIR";
            case "0501": return "STEREO";
            case "0502": return "[)(]PLII MOVIE";
            case "0503": return "[)(]PLIIx MOVIE";
            case "0504": return "Neo:6 CINEMA";
            case "0505": return "ALC Straight Decode";
            case "0506": return "[)(]DIGITAL EX";
            case "0507": return "[)(]PLIIx MOVIE";
            case "0508": return "DTS +Neo:6";
            case "0509": return "ES MATRIX";
            case "050a": return "ES DISCRETE";
            case "050b": return "DTS-ES 7.1";
            case "050c": return "XM HD Surround";
            case "050d": return "NEURAL SURR";
            case "050e": return "RETRIEVER AIR";
            case "0601": return "STEREO";
            case "0602": return "[)(]PLII MOVIE";
            case "0603": return "[)(]PLIIx MOVIE";
            case "0604": return "Neo:6 CINEMA";
            case "0605": return "STREAM DIRECT NORMAL Straight Decode";
            case "0606": return "[)(]DIGITAL EX";
            case "0607": return "[)(]PLIIx MOVIE";
            case "0608": return "(nothing)";
            case "0609": return "ES MATRIX";
            case "060a": return "ES DISCRETE";
            case "060b": return "DTS-ES 7.1";
            case "0701": return "STREAM DIRECT PURE 2ch";
            case "0702": return "[)(]PLII MOVIE";
            case "0703": return "[)(]PLIIx MOVIE";
            case "0704": return "Neo:6 CINEMA";
            case "0705": return "STREAM DIRECT PURE Straight Decode";
            case "0706": return "[)(]DIGITAL EX";
            case "0707": return "[)(]PLIIx MOVIE";
            case "0708": return "(nothing)";
            case "0709": return "ES MATRIX";
            case "070a": return "ES DISCRETE";
            case "070b": return "DTS-ES 7.1";
            case "0881": return "OPTIMUM";
            case "0e01": return "HDMI THROUGH";
            case "0f01": return "MULTI CH IN";
            default: {
                Log.d("FlStringConverter", "getPlayingListeningMode: unknown LM-Code: "+string);
                return "LM"+string;
            }
        }

    }
}
