package com.example.lib.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class MeasureMethodCostTimeAdapter extends AdviceAdapter {

    int startId = 0;
    private String methodName;
    private String className;

    public MeasureMethodCostTimeAdapter(int api, MethodVisitor methodVisitor, int methodAccess, String methodName,
                                        String methodDescriptor, String className) {
        super(api, methodVisitor, methodAccess, methodName, methodDescriptor);
        this.methodName = methodName;
        this.className = className;
    }

    @Override
    protected void onMethodEnter() {
        if (mv != null && !"<init>".equals(methodName)) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            startId = newLocal(Type.LONG_TYPE);
            mv.visitVarInsn(LSTORE, startId);
        }
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN || opcode <= RETURN)) {
            if (mv != null && !"<init>".equals(methodName)) {
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                mv.visitVarInsn(LLOAD, startId);
                mv.visitInsn(LSUB);
                int endId = newLocal(Type.LONG_TYPE);
                mv.visitVarInsn(LSTORE, endId);
                mv.visitLdcInsn(className);
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                mv.visitLdcInsn(methodName + ": cost = ");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                mv.visitVarInsn(LLOAD, endId);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
                mv.visitIntInsn(BIPUSH, 32);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(C)Ljava/lang/StringBuilder;", false);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                mv.visitMethodInsn(INVOKESTATIC, "com/example/lib/asm/PrintUtils", "print", "(Ljava/lang/String;Ljava/lang/String;)I", false);
                mv.visitInsn(POP);
            }
        }
        super.onMethodExit(opcode);
    }
}
