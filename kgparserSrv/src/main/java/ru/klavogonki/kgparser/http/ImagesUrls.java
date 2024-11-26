package ru.klavogonki.kgparser.http;

import ru.klavogonki.common.UrlConstructor;

/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 * $HeadURL$
 * $Author$
 * $Revision$
 * $Date::                      $
 * <br>
 * <br>
 * Класс, содержащий урлы картинок на клавогонках.
 */
public final class ImagesUrls {
    public static final String FIRST_SMILEY_URL = UrlConstructor.DOMAIN_NAME + "/img/smilies/first.gif";
    public static final String SECOND_SMILEY_URL = UrlConstructor.DOMAIN_NAME + "/img/smilies/second.gif";
    public static final String THIRD_SMILEY_URL = UrlConstructor.DOMAIN_NAME + "/img/smilies/third.gif";

    private ImagesUrls() {
    }
}
