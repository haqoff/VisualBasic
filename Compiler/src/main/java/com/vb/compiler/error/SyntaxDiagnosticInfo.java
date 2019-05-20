package com.vb.compiler.error;

/**
 * Represents class that contains information about parser error.
 *
 * @author haqoff
 */
public class SyntaxDiagnosticInfo {
    private ErrorCode code;
    private int offset;
    private int length;

    /**
     * Initializes new instance of {@see SyntaxDiagnosticInfo} with specified code, offset and length.
     *
     * @param code   Error code.
     * @param offset Offset.
     * @param length Error length.
     */
    public SyntaxDiagnosticInfo(ErrorCode code, int offset, int length) {
        this.code = code;
        this.offset = offset;
        this.length = length;
    }

    public ErrorCode getCode() {
        return code;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }
}
