<header>
    <table> <#-- todo: some modern layout like flex, is non-trivial with two rows and spaces between them -->
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
                <a href="./${links.normalTopBySpeedPage1}" class="normal">Обычный</a>
                | <a href="./${links.abraTopBySpeedPage1}" class="abra">Абракадабра</a>
                | <a href="./${links.referatsTopBySpeedPage1}" class="referats">Яндекс.Рефераты</a>
                | <a href="./${links.noErrorTopBySpeedPage1}" class="noerror">Безошибочный</a>
                | <a href="./${links.marathonTopBySpeedPage1}" class="marathon">Марафон</a>
                | <a href="./${links.charsTopBySpeedPage1}" class="chars">Буквы</a>
                | <a href="./${links.digitsTopBySpeedPage1}" class="digits">Цифры</a>
                | <a href="./${links.sprintTopBySpeedPage1}" class="sprint">Спринт</a>
                <div class="header-vertical-space"></div>
                <a href="./${links.normalInEnglishTopBySpeedPage1}">Обычный&nbsp;in&nbsp;English</a>
                | <a href="./${links.miniMarathonTopBySpeedPage1}">Мини-марафон,&nbsp;800&nbsp;знаков</a>
                | <a href="./${links.shortTextsTopBySpeedPage1}">Короткие&nbsp;тексты</a>
                | <a href="./${links.frequencyVocabularyTopBySpeedPage1}">Частотный&nbsp;словарь</a>
                | <a href="./${links.oneHundredRussianTopBySpeedPage1}">Соточка</a>
                | <a href="./${links.digitsOneHundredTopBySpeedPage1}">Цифросоточка</a>
                | <a href="./${links.trainingIndexFingersTopBySpeedPage1}">Тренируем&nbsp;указательные</a>
                <#-- todo: add some dictionary for middle fingers. Probably https://klavogonki.ru/vocs/5029/ :( -->
                | <a href="./${links.ringFingersTopBySpeedPage1}">Безымянные</a>
                | <a href="./${links.pinkiesPlusTopBySpeedPage1}">Мизинцы+</a>
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
