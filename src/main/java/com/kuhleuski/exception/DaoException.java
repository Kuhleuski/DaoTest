package com.kuhleuski.exception;

/**
 * Ошибка чтоб можно было отследить, что проблема именно в слое DAO
 * */

public class DaoException extends RuntimeException {

    public DaoException(Throwable throwable) {
        super(throwable);
    }
}
