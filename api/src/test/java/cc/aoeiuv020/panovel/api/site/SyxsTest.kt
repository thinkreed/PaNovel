package cc.aoeiuv020.panovel.api.site

import org.junit.Test

/**
 *
 * Created by AoEiuV020 on 2018.03.14-02:19:11.
 */
class SyxsTest : BaseNovelContextText(Syxs::class) {
    @Test
    fun search() {
        search("都市")
        search("一切从斗破苍穹开始", "千影残光", "7/7834")
        search("恐怖广播", "纯洁滴小龙", "0/265")
    }

    @Test
    fun detail() {
        detail("5/5821", "5/5821", "天刑纪", "曳光",
                "http://www.31xs.net/img/5/5821/5821s.jpg",
                "今朝修仙不为仙，只为春色花满园：来日九星冲牛斗，且看天刑开纪元。",
                "2018-05-20 23:33:08")
    }

    @Test
    fun chapters() {
        chapters("7/7787",
                "第一章 道法无情", "7/7787/5748209", null,
                "第八百九十六章 下落 二", "7/7787/10358281", null,
                892)
    }

    @Test
    fun content() {
        content("7/7787/10358281",
                "“一把自己都自身难保的刀，一个拼死也要救自己女儿的奇怪种族男人...”路胜隐隐泛起一丝兴趣。“也好，带我去看看，你女儿在哪？”",
                "而紫月浑身狠狠一颤，护体的金色枪花骤然破碎三朵，剩下六朵也齐齐一顿，被刚才那股恐怖的巨力震得酥麻战栗。",
                93)
    }
}
