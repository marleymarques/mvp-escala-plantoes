package br.com.marley.escala.exception;

public class RegraNegocioException extends RuntimeException
{
    public RegraNegocioException(String mensagem)
    {
        super(mensagem);
    }
}