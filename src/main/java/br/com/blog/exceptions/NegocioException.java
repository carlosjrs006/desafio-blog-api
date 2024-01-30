package br.com.blog.exceptions;

public class NegocioException extends RuntimeException {
    public NegocioException(String exception) {
        super(exception);
    }
}
