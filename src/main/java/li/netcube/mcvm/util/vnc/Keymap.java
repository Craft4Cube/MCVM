package li.netcube.mcvm.util.vnc;

import org.lwjgl.input.Keyboard;

import java.security.Key;

public class Keymap {
    /*public static int oldKeyConvert(int key, char ch) {
        //System.out.println("Key: " + key);
        //System.out.println(" Ch: " + ch);
        switch (key) {
            case Keyboard.KEY_BACK: return 0xff08;
            case Keyboard.KEY_TAB: return 0xff09;
            case Keyboard.KEY_RETURN: return 0xff0d;
            case Keyboard.KEY_ESCAPE: return 0xff1b;
            case Keyboard.KEY_INSERT: return 0xff63;
            case Keyboard.KEY_DELETE: return 0xffff;
            case Keyboard.KEY_HOME: return 0xff50;
            case Keyboard.KEY_END: return 0xff57;
            case 201: return 0xff55; //PG UP
            case 209: return 0xff56; //PG DN
            case Keyboard.KEY_LEFT: return 0xff51;
            case Keyboard.KEY_UP: return 0xff52;
            case Keyboard.KEY_RIGHT: return 0xff53;
            case Keyboard.KEY_DOWN: return 0xff54;
            case Keyboard.KEY_F1: return 0xffbe;
            case Keyboard.KEY_F2: return 0xffbf;
            case Keyboard.KEY_F3: return 0xffc0;
            case Keyboard.KEY_F4: return 0xffc1;
            case Keyboard.KEY_F5: return 0xffc2;
            case Keyboard.KEY_F6: return 0xffc3;
            case Keyboard.KEY_F7: return 0xffc4;
            case Keyboard.KEY_F8: return 0xffc5;
            case Keyboard.KEY_F9: return 0xffc6;
            case Keyboard.KEY_F10: return 0xffc7;
            case Keyboard.KEY_F11: return 0xffc8;
            case Keyboard.KEY_F12: return 0xffc9;
            case Keyboard.KEY_LSHIFT: return 0xffe1;
            case Keyboard.KEY_RSHIFT: return 0xffe1;
            case Keyboard.KEY_LCONTROL: return 0xffe3;
            case Keyboard.KEY_RCONTROL: return 0xffe3;
            case Keyboard.KEY_LMETA: return 0xffe7;
            case Keyboard.KEY_RMETA: return 0xffe7;
            case 56: return 0xffe9; //ALT
        }
        return ch;
    }*/

    public static int keyConvert(int key, int ch) {
        switch (key) {
            case Keyboard.KEY_ESCAPE:      return 0xFF1B;
            case Keyboard.KEY_1:           return 0x31;
            case Keyboard.KEY_2:           return 0x32;
            case Keyboard.KEY_3:           return 0x33;
            case Keyboard.KEY_4:           return 0x34;
            case Keyboard.KEY_5:           return 0x35;
            case Keyboard.KEY_6:           return 0x36;
            case Keyboard.KEY_7:           return 0x37;
            case Keyboard.KEY_8:           return 0x38;
            case Keyboard.KEY_9:           return 0x39;
            case Keyboard.KEY_0:           return 0x30;
            case 26:                       return 0x2D; //MINUS
            case 27:                       return 0x3D; //EQUALS
            case Keyboard.KEY_BACK:        return 0xFF08;
            case Keyboard.KEY_TAB:         return 0xFF09;
            case Keyboard.KEY_Q:           if (isUpperCase(ch)) return 0x51; else return 0x71;
            case Keyboard.KEY_W:           if (isUpperCase(ch)) return 0x57; else return 0x77;
            case Keyboard.KEY_E:           if (isUpperCase(ch)) return 0x45; else return 0x65;
            case Keyboard.KEY_R:           if (isUpperCase(ch)) return 0x52; else return 0x72;
            case Keyboard.KEY_T:           if (isUpperCase(ch)) return 0x54; else return 0x74;
            case Keyboard.KEY_Y:           if (isUpperCase(ch)) return 0x59; else return 0x79;
            case Keyboard.KEY_U:           if (isUpperCase(ch)) return 0x55; else return 0x75;
            case Keyboard.KEY_I:           if (isUpperCase(ch)) return 0x49; else return 0x69;
            case Keyboard.KEY_O:           if (isUpperCase(ch)) return 0x4F; else return 0x6F;
            case Keyboard.KEY_P:           if (isUpperCase(ch)) return 0x50; else return 0x70;
            case 39:                       return 0x5B; //LBRACKET
            case 13:                       return 0x5D; //RBRACKET
            case Keyboard.KEY_RETURN:      return 0xFF0D;
            case Keyboard.KEY_LCONTROL:    return 0xFFE3;
            case Keyboard.KEY_A:           if (isUpperCase(ch)) return 0x41; else return 0x61;
            case Keyboard.KEY_S:           if (isUpperCase(ch)) return 0x53; else return 0x73;
            case Keyboard.KEY_D:           if (isUpperCase(ch)) return 0x44; else return 0x64;
            case Keyboard.KEY_F:           if (isUpperCase(ch)) return 0x46; else return 0x66;
            case Keyboard.KEY_G:           if (isUpperCase(ch)) return 0x47; else return 0x67;
            case Keyboard.KEY_H:           if (isUpperCase(ch)) return 0x48; else return 0x68;
            case Keyboard.KEY_J:           if (isUpperCase(ch)) return 0x4A; else return 0x6A;
            case Keyboard.KEY_K:           if (isUpperCase(ch)) return 0x4B; else return 0x6B;
            case Keyboard.KEY_L:           if (isUpperCase(ch)) return 0x4C; else return 0x6C;
            case 41:                       return 0x3B; //SEMICOLON
            case Keyboard.KEY_APOSTROPHE:  return 0x27;
            case 43:                       return 0x60; //GRAVE
            case Keyboard.KEY_LSHIFT:      return 0xFFE1;
            case 53:                       return 0x5C; //BACKSLASH
            case Keyboard.KEY_Z:           if (isUpperCase(ch)) return 0x5A; else return 0x7A;
            case Keyboard.KEY_X:           if (isUpperCase(ch)) return 0x58; else return 0x78;
            case Keyboard.KEY_C:           if (isUpperCase(ch)) return 0x43; else return 0x63;
            case Keyboard.KEY_V:           if (isUpperCase(ch)) return 0x56; else return 0x76;
            case Keyboard.KEY_B:           if (isUpperCase(ch)) return 0x42; else return 0x62;
            case Keyboard.KEY_N:           if (isUpperCase(ch)) return 0x4E; else return 0x6E;
            case Keyboard.KEY_M:           if (isUpperCase(ch)) return 0x4D; else return 0x6D;
            case Keyboard.KEY_COMMA:       return 0x2C;
            case Keyboard.KEY_PERIOD:      return 0x2E;
            case 12:                       return 0x2F; //SLASH
            case Keyboard.KEY_RSHIFT:      return 0xFFE2;
            case Keyboard.KEY_MULTIPLY:    return 0xFFAA;
            case Keyboard.KEY_LMENU:       return 0xFFE9;
            case Keyboard.KEY_SPACE:       return 0x20;
            case Keyboard.KEY_CAPITAL:     return 0xFFE5;
            case Keyboard.KEY_F1:          return 0xFFBE;
            case Keyboard.KEY_F2:          return 0xFFBF;
            case Keyboard.KEY_F3:          return 0xFFC0;
            case Keyboard.KEY_F4:          return 0xFFC1;
            case Keyboard.KEY_F5:          return 0xFFC2;
            case Keyboard.KEY_F6:          return 0xFFC3;
            case Keyboard.KEY_F7:          return 0xFFC4;
            case Keyboard.KEY_F8:          return 0xFFC5;
            case Keyboard.KEY_F9:          return 0xFFC6;
            case Keyboard.KEY_F10:         return 0xFFC7;
            case Keyboard.KEY_NUMLOCK:     return 0xFF7F;
            case Keyboard.KEY_SCROLL:      return 0xFF14;
            case Keyboard.KEY_NUMPAD7:     if (isNumber(ch)) return 0x37; else return 0xFF95;
            case Keyboard.KEY_NUMPAD8:     if (isNumber(ch)) return 0x38; else return 0xFF97;
            case Keyboard.KEY_NUMPAD9:     if (isNumber(ch)) return 0x39; else return 0xFF9A;
            case Keyboard.KEY_SUBTRACT:    return 0xFFAD;
            case Keyboard.KEY_NUMPAD4:     if (isNumber(ch)) return 0x34; else return 0xFF96;
            case Keyboard.KEY_NUMPAD5:     if (isNumber(ch)) return 0x35; else return 0xFF9D;
            case Keyboard.KEY_NUMPAD6:     if (isNumber(ch)) return 0x36; else return 0xFF98;
            case Keyboard.KEY_ADD:         return 0xFFAB;
            case Keyboard.KEY_NUMPAD1:     if (isNumber(ch)) return 0x31; else return 0xFF9C;
            case Keyboard.KEY_NUMPAD2:     if (isNumber(ch)) return 0x32; else return 0xFF99;
            case Keyboard.KEY_NUMPAD3:     if (isNumber(ch)) return 0x33; else return 0xFF9B;
            case Keyboard.KEY_NUMPAD0:     if (isNumber(ch)) return 0x30; else return 0xFF9E;
            case Keyboard.KEY_NUMPADCOMMA: if (ch == 0x2C) return 0x2C; else return 0xFF9F;
            case Keyboard.KEY_F11:         return 0xFFC8;
            case Keyboard.KEY_F12:         return 0xFFC9;
            case Keyboard.KEY_CONVERT:     return 0xFF23;
            case Keyboard.KEY_NUMPADENTER: return 0xFF8D;
            case Keyboard.KEY_RCONTROL:    return 0xFFE4;
            case Keyboard.KEY_DIVIDE:      return 0xFFAF;
            case Keyboard.KEY_RMENU:       return 0xFFEA;
            case Keyboard.KEY_HOME:        return 0xFF50;
            case Keyboard.KEY_UP:          return 0xFF52;
            case Keyboard.KEY_PRIOR:       return 0xFF55;
            case Keyboard.KEY_LEFT:        return 0xFF51;
            case Keyboard.KEY_RIGHT:       return 0xFF53;
            case Keyboard.KEY_END:         return 0xFF57;
            case Keyboard.KEY_DOWN:        return 0xFF54;
            case Keyboard.KEY_NEXT:        return 0xFF56;
            case Keyboard.KEY_INSERT:      return 0xFF63;
            case Keyboard.KEY_DELETE:      return 0xFFFF;
            case Keyboard.KEY_LMETA:       return 0xFFE7;
            case Keyboard.KEY_RMETA:       return 0xFFE8;
            case Keyboard.KEY_APPS:        return 0xFF67;
            default:                       return ch;
        }
    }

    private static boolean isUpperCase(int ch) {
        return (ch >= 0x41 && ch <= 0x5A);
    }

    private static boolean isNumber(int ch) {
        return (ch >= 0x30 && ch <= 0x39);
    }
}
