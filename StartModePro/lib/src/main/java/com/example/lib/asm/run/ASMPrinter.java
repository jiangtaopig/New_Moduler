package com.example.lib.asm.run;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;


public class ASMPrinter {
    public static void main(String[] args) {
        String className = "com.example.lib.asm.run.Tasm";
        int parsingOptions = ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG;
        boolean asmCode = true;

        // (2) 打印结果
        Printer printer = asmCode ? new ASMifier() : new Textifier();
        PrintWriter printWriter = new PrintWriter(System.out, true);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, printWriter);
        try {
            new ClassReader(className).accept(traceClassVisitor, parsingOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
