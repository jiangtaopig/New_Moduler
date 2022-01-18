package com.example.lib;

import com.example.lib.asm.Han;

public class TT {
    private void showHan(String val) {
        Han.show(val);

        Thread.interrupted();
    }
}
