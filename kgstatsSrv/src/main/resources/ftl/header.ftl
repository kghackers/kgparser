<header>
    <table role="presentation"> <#-- todo: some modern layout like flex, is non-trivial with two rows and spaces between them -->
        <tr>
            <td class="header-left">
                <a href="./${links.index}">На главную</a>
                | <a href="https://github.com/dmitry-weirdo/kgparser/">Kgparser&nbsp;on&nbsp;GitHub</a>
                | <a href="https://klavogonki.ru/forum/software/59/">Тема&nbsp;на&nbsp;форуме</a>
                | By <a href="https://klavogonki.ru/u/#/242585/">nosferatum</a>

                <div class="header-vertical-space"></div>

                <a href="./${links.topByTotalRaces}">Топ&nbsp;по&nbsp;общему&nbsp;пробегу</a>
                | <a href="./${links.topByBestSpeedPage1}">Топ по рекорду в &laquo;Обычном&raquo;</a>
                | <a href="./${links.topByBestSpeed}">Топ-500&nbsp;в&nbsp;&laquo;Обычном&raquo;</a> <#-- todo: think whether we need it -->
                | <a href="./${links.topByRatingLevel}">Топ&nbsp;по&nbsp;уровню</a>
                | <a href="./${links.topByFriendsCount}">Топ&nbsp;по&nbsp;числу&nbsp;друзей</a>
                | <a href="./${links.topByAchievementsCount}">Топ&nbsp;по&nbsp;числу&nbsp;достижений</a>

                <div class="header-vertical-space"></div>

                <a href="./${links.topByVocabulariesCount}">Топ&nbsp;по&nbsp;числу&nbsp;используемых&nbsp;словарей</a>
                | <a href="./${links.topByCarsCount}">Топ&nbsp;по&nbsp;числу&nbsp;машин&nbsp;в&nbsp;гараже</a>
                | <a href="./${links.playersByRank}">Игроки&nbsp;по&nbsp;рангам</a>

                <div class="header-vertical-space"></div>

                <#-- todo: typical header should be exracted to an ftl template -->
                <a href="./${links.normalTopBySpeedPage1}" class="normal">${links.normalTopHeader}</a>
                | <a href="./${links.abraTopBySpeedPage1}" class="abra">${links.abraTopHeader}</a>
                | <a href="./${links.referatsTopBySpeedPage1}" class="referats">${links.referatsTopHeader}</a>
                | <a href="./${links.noErrorTopBySpeedPage1}" class="noerror">${links.noErrorTopHeader}</a>
                | <a href="./${links.marathonTopBySpeedPage1}" class="marathon">${links.marathonTopHeader}</a>
                | <a href="./${links.charsTopBySpeedPage1}" class="chars">${links.charsTopHeader}</a>
                | <a href="./${links.digitsTopBySpeedPage1}" class="digits">${links.digitsTopHeader}</a>
                | <a href="./${links.sprintTopBySpeedPage1}" class="sprint">${links.sprintTopHeader}</a>

                <div class="header-vertical-space"></div>

                <#-- todo: extract getting header and into sub-template .ftl if it's possible to have template within template -->
                <#assign nonStandardDictionaries = links.nonStandardDictionaries>
                
                <#-- Обычный in English -->
                <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(5539)}">${nonStandardDictionaries.getHeaderName(5539)}</a>

                <#-- Мини-марафон, 800 знаков -->
                | <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(6018)}">${nonStandardDictionaries.getHeaderName(6018)}</a>

                <#-- Короткие тексты -->
                | <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(1789)}">${nonStandardDictionaries.getHeaderName(1789)}</a>

                <#-- Частотный словарь -->
                | <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(192)}">${nonStandardDictionaries.getHeaderName(192)}</a>

                <#-- Соточка -->
                | <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(25856)}">${nonStandardDictionaries.getHeaderName(25856)}</a>

                <#-- Цифросоточка -->
                | <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(62238)}">${nonStandardDictionaries.getHeaderName(62238)}</a>

                <#-- Тренируем указательные -->
                | <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(226)}">${nonStandardDictionaries.getHeaderName(226)}</a>

                <#-- todo: add some dictionary for middle fingers. Probably https://klavogonki.ru/vocs/5029/ :( -->

                <#-- Безымянные -->
                | <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(8223)}">${nonStandardDictionaries.getHeaderName(8223)}</a>

                <#-- Мизинцы+ -->
                | <a href="./${nonStandardDictionaries.getTopBySpeedFirstPageFilePath(3714)}">${nonStandardDictionaries.getHeaderName(3714)}</a>

                <div class="header-vertical-space"></div>

                <a href="./2020-12-09/stats.html">Статистика&nbsp;от&nbsp;09.12.2020</a>
            </td>

            <td class="header-right" style="width: 300px;">
                <form method="POST" action="https://yoomoney.ru/quickpay/confirm.xml" style="padding: 5px 0 0 0; width: 350px;">
                    <img src="./img/iomoney_logo_white.png" width="100" height="22" alt="ЮMoney"/>
                    <div class="header-vertical-space"></div>
                    <input type="hidden" name="receiver" value="4100116361083369">
                    <input type="hidden" name="formcomment" value="Добровольный донат за Клавостатистику (1)">
                    <input type="hidden" name="short-dest" value="Добровольный донат за Клавостатистику (2)">
                    <input type="hidden" name="label" value="$order_id">
                    <input type="hidden" name="quickpay-form" value="donate">
                    <input type="hidden" name="targets" value="Донат хорошим людям за Клавостат!">
                    <input type="text" name="sum" value="200" data-type="number" required pattern="[0-9]+">&nbsp;руб.
                    <div class="header-vertical-space"></div>
                    <input type="text" name="comment" style="width: 220px;" value="Донат за Клавостат :)" maxlength="200">
                    <input type="hidden" name="need-fio" value="false">
                    <input type="hidden" name="need-email" value="false">
                    <input type="hidden" name="need-phone" value="false">
                    <input type="hidden" name="need-address" value="false">
                    <div class="header-vertical-space"></div>
                    <label><input type="radio" name="paymentType" value="PC">ЮMoney</label>
                    <label><input type="radio" name="paymentType" value="AC" checked>Картой</label>
                    <input type="submit" value="Перевести">
                </form>
            </td>

            <td class="header-right" style="width: 100px;">
                <form action="https://www.paypal.com/donate" method="post" target="_top">
                    <input type="hidden" name="hosted_button_id" value="WZ2BM3QJYGQTW"/>
                    <input type="image" src="./img/paypal-logo-small.webp" border="0"
                           name="submit" title="PayPal - The safer, easier way to pay online!" alt="Donate with PayPal button"/>
                    <img alt="" border="0" src="https://www.paypal.com/en_DE/i/scr/pixel.gif" width="1" height="1"/>
                </form>
                <div class="header-vertical-space"></div>
                <div class="header-vertical-space"></div>
                Или на карту Тинькофф&nbsp;&mdash; 5536&nbsp;9139&nbsp;2010&nbsp;2668
            <td>
        </tr>
    </table>
</header>
