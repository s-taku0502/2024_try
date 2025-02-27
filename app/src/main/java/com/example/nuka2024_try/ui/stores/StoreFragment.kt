package com.example.nuka2024_try.ui.stores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.R
import com.example.nuka2024_try.databinding.FragmentStoresBinding
import java.net.URL

class StoreFragment : Fragment() {

    private var _binding: FragmentStoresBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoresBinding.inflate(inflater, container, false)
        val root = binding.root

        // サンプルデータ
        val storeList = listOf(
            Store(
                "株式会社アウリエ",
                "アウリエの詳細情報",
                "住宅・不動産・ホテル",
                "不動産を通じて、人と地域をつなぐ",
                "石川県金沢市大額2丁目65番地",
                URL("https://aurie.jp/"),
                R.drawable.aurie
            ),
            Store(
                "株式会社アスティ",
                "アスティの詳細情報",
                "老人福祉・介護事業",
                "介護事業の将来を守る温かい心のケア",
                "石川県金沢市四十万４丁目１２７－２",
                URL("https://asty-k.jp/"),
                R.drawable.asutei
            ),
            Store(
                "石崎産業株式会社",
                "石崎産業の詳細情報",
                "包装機械製造業",
                "環境配慮と地域貢献を重視した包装資材企業",
                "石川県金沢市大額3丁目194番地",
                URL("http://www.ishizaki-sangyo.com/"),
                R.drawable.ishizakisangyou
            ),
            Store(
                "いしだ鍼灸治療院",
                "いしだ鍼灸治療院の詳細情報",
                "医療・美容・介護",
                "口コミで支持を集める鍼灸治療院",
                "金沢市額谷3-61 サンライフⅠ 102",
                URL("https://ishida2982020.web.fc2.com/"),
                R.drawable.ishidashinkyuuchiryouin
            ),
            Store(
                "Food&Drink 今伴",
                "Food&Drink 今伴の詳細情報",
                "飲食業",
                "名物の「焼き豚足」は食べなきゃ損！",
                "金沢市有松2-15-3",
                URL("https://nukashinkoukai.com/shop/33/"),
                R.drawable.imahan
            ),
            Store(
                "インデアンカレー金沢額谷店",
                "インデアンカレー金沢額谷店の詳細情報",
                "飲食業",
                "セットメニューや新作で他店と差別化を図る新鋭！",
                "金沢市額谷3-102",
                URL("https://www.instagram.com/indian.nukadani/"),
                R.drawable.indian_curry
            ),
            Store(
                "エイム SOUTH FORT",
                "エイム SOUTH FORTの詳細情報",
                "フィットネス",
                "健康サービス活動を通じて社会生活の改善と向上を図る",
                "金沢市大額2-57",
                URL("https://www.fitness-aim.com/southfort/"),
                R.drawable.aim_southfort
            ),
            Store(
                "オリジナル企画",
                "オリジナル企画の詳細情報",
                "サービス・保険業",
                "クルマ・保険・福祉をワンストップサービスで提供",
                "金沢市額谷3-49",
                URL("http://www.original-kikaku.com/"),
                R.drawable.original_event
            ),
            Store(
                "オリジナル サポート",
                "オリジナル サポートの詳細情報",
                "老人福祉・介護事業",
                "「障がいがあっても輝ける社会へ」を理念に",
                "金沢市額谷3-96",
                URL("https://sm-group.or.jp/business/"),
                R.drawable.original_support
            ),
            Store(
                "カーサービス Y-TECH",
                "カーサービス Y-TECHの詳細情報",
                "自動車業",
                "安全と安心を人々に提供する",
                "金沢市額谷3-97",
                URL("https://nukashinkoukai.com/shop/404/"),
                R.drawable.car_service
            ),
            Store(
                "金沢国際ホテル",
                "金沢国際ホテルの詳細情報",
                "ホテル業",
                "地元客のリピーターが多い、金沢南部のランドマーク的ホテル",
                "金沢市大額町ル-8",
                URL("http://www.kanazawakokusaihotel.co.jp/"),
                R.drawable.kanazawakokusai_hotel
            ),
            Store(
                "金沢信用金庫 額支店",
                "金沢信用金庫 額支店の詳細情報",
                "金融業",
                "「金融サービス業」として、域経済の発展に貢献",
                "金沢市高尾南3-106",
                URL("http://www.shinkin.co.jp/kanazawa/index.html"),
                R.drawable.kanazawasinyoukinko
            ),
            Store(
                "金正商事",
                "金正商事の詳細情報",
                "設営業",
                "豊富な実績と経験で、替えのきかない“設営のプロ",
                "金沢市問屋町2-109-4",
                URL("https://kanasyo.themedia.jp/"),
                R.drawable.kanazawasyouji
            ),
            Store(
                "カプセルHOSTEL「金沢家」",
                "カプセルHOSTEL「金沢家」の詳細情報",
                "ホテル業",
                "充実したアメニティと低価格で満室御礼！",
                "金沢市高尾南2-68",
                URL("https://www.kanazawaya.co.jp"),
                R.drawable.kapuseruhotel
            ),
            Store(
                "すなっく 京華",
                "すなっく 京華の詳細情報",
                "飲食業",
                "歌手としての顔も持つ多趣味人",
                "金沢市高尾台4-16",
                URL("https://nukashinkoukai.com/shop/408/"),
                R.drawable.sunakkukyouka
            ),
            Store(
                "中国家庭料理 京香",
                "中国家庭料理 京香の詳細情報",
                "飲食業",
                "本場の味を安く、早く、お腹いっぱい味わうなら！",
                "金沢市窪3-325",
                URL("https://nukashinkoukai.com/shop/1259/"),
                R.drawable.chuka
            ),
            Store(
                "響伸仮設",
                "響伸仮設の詳細情報",
                "建設業",
                "豊富な経験で、建築工事の足元をサポート",
                "金沢市中屋町西444",
                URL("https://nukashinkoukai.com/shop/811/"),
                R.drawable.kyousin
            ),
            Store(
                "Cook どれみ",
                "Cook どれみの詳細情報",
                "飲食業",
                "名物メニューと人柄の良さで常連さんのハートをつかむ！",
                "金沢市三十苅町乙157　額中央ビル1階テナント",
                URL("https://www.instagram.com/cook_doremi"),
                R.drawable.cook
            ),
            Store(
                "相撲茶屋 玄海",
                "相撲茶屋 玄海の詳細情報",
                "飲食業",
                "昼はラーメン、夜は居酒屋！元力士がつくるこだわりの一品",
                "金沢市光が丘1-124",
                URL("https://nukashinkoukai.com/shop/943/"),
                R.drawable.sumou
            ),
            Store(
                "建匠",
                "建匠の詳細情報",
                "建築業",
                "多様なニーズに応える地域密着の工務店",
                "金沢市額新保1-169-2",
                URL("https://kensho-kanazawa.com/"),
                R.drawable.kensyou
            ),
            Store(
                "興能信用金庫 額支店",
                "興能信用金庫 額支店の詳細情報",
                "金融業",
                "「地域にまっすぐ」なファーストコール・バンク",
                "金沢市額乙丸町ハ80",
                URL("https://www.kono-shinkin.co.jp/branch/257/"),
                R.drawable.kounosinyoukinko
            ),
            Store(
                "コープ おおぬか",
                "コープ おおぬかの詳細情報",
                "サービス業",
                "買い物に来る度、新しい発見がある店",
                "金沢市大額2-50",
                URL("https://www.ishikawa.coop/shop/ohnuka/"),
                R.drawable.coupoonuka
            ),
            Store(
                "コスモシティ",
                "コスモシティの詳細情報",
                "不動産業",
                "住まいの最新情報館",
                "金沢市高尾台4-171",
                URL("http://cosmo-city.com/"),
                R.drawable.cosumosity
            ),
            Store(
                "サイクルショップ カガ",
                "サイクルショップ カガの詳細情報",
                "自転車業",
                "メンテナンスとアフターサービスに高い評価",
                "金沢市額乙丸町イ29-1",
                URL("https://www.cskaga.jp/"),
                R.drawable.cyicle
            ),
            Store(
                "蛇之目寿司",
                "蛇之目寿司の詳細情報",
                "飲食業",
                "40年愛される、昔ながらのお寿司屋さん",
                "石川県金沢市大額2-55",
                URL("http://www.kanazawa-jyanome.com/"),
                R.drawable.janomezusi
            ),
            Store(
                "新住宅",
                "新住宅の詳細情報",
                "不動産業",
                "「生涯の家」",
                "金沢市額乙丸町ロ257",
                URL("http://shinjutaku.com/"),
                R.drawable.sinjuutaku
            ),
            Store(
                "新日本ツーリスト",
                "新日本ツーリストの詳細情報",
                "旅行業",
                "問題を解決する“オーダーメイドの旅”を提案",
                "野々市市押野2-250",
                URL("http://nj-tourist.com/"),
                R.drawable.shinnihonn
            ),
            Store(
                "セゾン ビューティーサロン",
                "セゾン ビューティーサロンの詳細情報",
                "美容業",
                "お客さまの「キレイになりたい」をお手伝い",
                "金沢市大額町ル8 金沢国際ホテル地下1F",
                URL("https://saison-esthe.com/"),
                R.drawable.sezon
            ),
            Store(
                "セゾン ビューティーサロン",
                "セゾン ビューティーサロンの詳細情報",
                "美容業",
                "お客さまの「キレイになりたい」をお手伝い",
                "金沢市大額町ル8 金沢国際ホテル地下1F",
                URL("https://saison-esthe.com/"),
                R.drawable.sezon
            ),
            Store(
                "やきとり 膳",
                "やきとり 膳の詳細情報",
                "飲食業",
                "手づくり焼き鳥で常連を魅了する居酒屋",
                "石川県金沢市窪4-403　本陣ロイヤルハイム1F",
                URL("https://nukashinkoukai.com/shop/417/"),
                R.drawable.yakitori_zen
            ),
            Store(
                "鯛平楽（移動販売）",
                "鯛平楽（移動販売）の詳細情報",
                "飲食業",
                "うまいをどこへでも",
                "白山市鶴来今町ツ73-4",
                URL("https://nukashinkoukai.com/shop/418/"),
                R.drawable.taiheiraku
            ),
            Store(
                "高木歯科医院",
                "高木歯科医院の詳細情報",
                "医療業",
                "確かな技術と思いやりあふれる姿勢に信頼",
                "金沢市額谷3-105",
                URL("https://nukashinkoukai.com/shop/419/"),
                R.drawable.takagi
            ),
            Store(
                "高木歯科医院",
                "高木歯科医院の詳細情報",
                "医療業",
                "確かな技術と思いやりあふれる姿勢に信頼",
                "金沢市額谷3-105",
                URL("https://nukashinkoukai.com/shop/419/"),
                R.drawable.takagi
            ),
            Store(
                "たこやき太郎",
                "たこやき太郎の詳細情報",
                "飲食業",
                "地域の人のお腹を満たす、絶品たこ焼き!",
                "石川県金沢市額新保1-445",
                URL("https://nukashinkoukai.com/shop/420/"),
                R.drawable.takoyaki_tarou
            ),
            Store(
                "寿司・一品料理 だんち",
                "寿司・一品料理 だんちの詳細情報",
                "飲食業",
                "仲良し夫婦が元気に切り盛り",
                "金沢市大額1-17",
                URL("https://nukashinkoukai.com/shop/421/"),
                R.drawable.danti
            ),
            Store(
                "JOHNかりおすとろ ちむちむ堂",
                "JOHNかりおすとろ ちむちむ堂の詳細情報",
                "飲食業",
                "名物料理のオンパレードで、あらゆる客層に愛される！",
                "金沢市額新保2-251アバンザ１F",
                URL("https://johnkariosutoro-chimuchimudo.jimdosite.com/"),
                R.drawable.john
            ),
            Store(
                "ツジヨシ",
                "ツジヨシの詳細情報",
                "建築業",
                "「きめ細かく、即対応」で住空間をデザイン",
                "金沢市光が丘3-255-2",
                URL("http://www.tujiyoshi.co.jp/"),
                R.drawable.tujiyosi
            ),
            Store(
                "ディーセントワーク",
                "ディーセントワークの詳細情報",
                "コンサルタント業",
                "豊富な経験による適職相談で「選択肢を増やす」お手伝い",
                "野々市市末松3-570 いしかわ大学連携インキュベータ",
                URL("https://decent-work.co.jp"),
                R.drawable.disento
            ),
            Store(
                "中野玩具",
                "中野玩具の詳細情報",
                "サービス業",
                "原動力は「子どもの笑顔」！",
                "野々市市押野5-212",
                URL("https://twitter.com/8751Toy"),
                R.drawable.nakano
            ),
            Store(
                "日章警備保障",
                "日章警備保障の詳細情報",
                "サービス業",
                "「地域の安全・安心・秩序を守っている」誇りを胸に",
                "金沢市粟崎町ホ110-34",
                URL("http://www.nissho.info/"),
                R.drawable.keibi
            ),
            Store(
                "スナック NORI",
                "スナック NORIの詳細情報",
                "飲食業",
                "リーズナブルに楽しく飲める人気のスナック",
                "金沢市額新町2-33-2",
                URL("https://nukashinkoukai.com/shop/426/"),
                R.drawable.sunaku
            ),
            Store(
                "ハート保険ジャパン",
                "ハート保険ジャパンの詳細情報",
                "金融業",
                "的確なプラン提示と親身なサポートで“まかせてよかった",
                "金沢市額新保1-273 コンテントノース102",
                URL("http://www.sjnk-ag.com/a/hhj/"),
                R.drawable.harto
            ),
            Store(
                "patisserie MACIMA",
                "patisserie MACIMAの詳細情報",
                "洋菓子業",
                "たゆまぬ創作心が生む、珠玉のケーキに注目",
                "金沢市大額3-267 1F",
                URL("https://www.instagram.com/patisserie.macima/?igshid=2ye608mi7zd5"),
                R.drawable.cake
            ),
            Store(
                "株式会社パレネ",
                "株式会社パレネの詳細情報",
                "洋菓子業",
                "誠心誠意” をモットーにあらゆるビジネスシーンをサポート!!",
                "野々市市扇が丘27-7",
                URL("https://www.parrainer.jp/"),
                R.drawable.parrainer
            ),
            Store(
                "バロー金沢高尾店",
                "バロー金沢高尾店の詳細情報",
                "サービス業",
                "採れたて新鮮",
                "金沢市高尾台3-45",
                URL("https://valor.jp/stores/201/"),
                R.drawable.valor
            ),
            Store(
                "Palos Life Consulting",
                "Palos Life Consultingの詳細情報",
                "金融業",
                "対話を重ねることで保険の疑問を解決",
                "金沢市額新保1-273　104号室",
                URL("https://palos.jp/"),
                R.drawable.palos
            ),
            Store(
                "ばんき和漢堂薬局・鍼灸院",
                "ばんき和漢堂薬局・鍼灸院の詳細情報",
                "医療業",
                "ココロと体の悩みを「漢方薬」「鍼灸治療」でサポート",
                "金沢市大額2-35",
                URL("https://bankiwakandou.com/"),
                R.drawable.banki
            ),
            Store(
                "焼肉ひで華 大額店",
                "焼肉ひで華 大額店の詳細情報",
                "飲食業",
                "折り紙付きの旨い肉と、心地よい接客",
                "金沢市大額1-291",
                URL("https://nukashinkoukai.com/shop/432/"),
                R.drawable.hide
            ),
            Store(
                "ファブールののいちサロン",
                "ファブールののいちサロンの詳細情報",
                "美容業",
                "高い技術とサービスで極上のリラックスタイムを",
                "野々市市粟田2-77-7-B",
                URL("https://faveur-salon.com/"),
                R.drawable.nonoichisaron
            ),
            Store(
                "BOO BOO（移動販売）",
                "BOO BOOの詳細情報",
                "飲食業",
                "いつでもなるべく焼きたてをお届け！",
                "白山市宮永市町114-6",
                URL("https://www.instagram.com/booboo.yamaguchi/"),
                R.drawable.boo
            ),
            Store(
                "BOO BOOZ",
                "BOO BOOZの詳細情報",
                "自動車業",
                "独自ルートと確かな目利きで、買い取りに絶対の自信！",
                "金沢市大額3-170",
                URL("https://www.goo-net.com/usedcar_shop/1157148/detail.html"),
                R.drawable.boo_booz
            ),
            Store(
                "古谷不動産",
                "古谷不動産の詳細情報",
                "不動産業",
                "家庭的な対応で愛され続けて半世紀",
                "金沢市高尾南3-86",
                URL("http://www.furutani-f.com/"),
                R.drawable.furutani
            ),
            Store(
                "北國銀行 額支店",
                "北國銀行 額支店の詳細情報",
                "金融業",
                "豊かな明日へ、信頼の架け橋を",
                "金沢市大額2-26",
                URL("https://sasp.mapion.co.jp/b/hokkokubank/info/1132/"),
                R.drawable.hokkoku
            ),
            Store(
                "北陸銀行 光が丘支店",
                "北陸銀行 光が丘支店の詳細情報",
                "金融業",
                "「お客様の財産を守るのが責務」",
                "金沢市光が丘1-127",
                URL("https://www.hokugin.co.jp/branch/detail/105/"),
                R.drawable.hokkoku_2
            ),
            Store(
                    "北陸経営保全プラニング",
                    "北陸経営保全プラニングの詳細情報",
                    "金融業",
                    "豊富な実績と経験で、未来の安心をサポート",
                "金沢市額谷3-101",
                URL("https://www.hkp-planing.com/"),
                R.drawable.hokuriku
            ),
            Store(
                "北國新聞文化センター 金沢南スタジオ",
                "北國新聞文化センター 金沢南スタジオの詳細情報",
                "フィットネス業",
                "老若男女が集う北陸最大のカルチャースクール",
                "金沢市額谷町ホ2-8",
                URL("http://hokkoku.bunkacenter.or.jp/studio/studio"),
                R.drawable.stadio
            ),
            Store(
                "割烹 みや川",
                "割烹 みや川の詳細情報",
                "飲食業",
                "「一客一亭」の心で、一人ひとりにおもてなし",
                "金沢市額新保1-431",
                URL("http://ishikawa-kappou-miyakawa.com/"),
                R.drawable.kappou
            ),
            Store(
                "もみほぐし手楽",
                "もみほぐし手楽の詳細情報",
                "美容業",
                "フレンドリーな従兄弟セラピストがもみほぐし",
                "金沢市大額3-255",
                URL("https://www.momihogusi-tera.com/"),
                R.drawable.teraku
            ),
            Store(
                "山本歯科医院",
                "山本歯科医院の詳細情報",
                "医療業",
                "患者ファースト”で地域に信頼され30年",
                "金沢市円光寺2-3-1",
                URL("https://haisha-yoyaku.jp/bun2sdental/detail/index/id/17301339"),
                R.drawable.yamamoto
            ),
            Store(
                "リーマ",
                "リーマの詳細情報",
                "繊維業",
                "「利真於動」の心でお客様の利益に貢献",
                "野々市市三日市2-227",
                URL("http://www.towel-white.com"),
                R.drawable.rima
            ),
            Store(
                "ガールズバーROSE",
                "ガールズバーROSEの詳細情報",
                "飲食業",
                "リーズナブルに楽しめるガールズバー",
                "金沢市額新町1-25-2",
                URL("https://rose-kanazawa.jimdofree.com/"),
                R.drawable.rose
            ),
            Store(
                "額振興会事務局",
                "額振興会事務局の詳細情報",
                "サービス業",
                "額振興会への入会は随時受付中！お気軽にお電話ください。",
                "金沢市額新保1-169-2　建匠2F",
                URL("https://nukashinkoukai.com/shop/1206/"),
                R.drawable.nuka
            )
        )

        // RecyclerView のセットアップ
        val recyclerView = binding.storeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // 1列表示
        recyclerView.adapter = StoreAdapter(storeList) // アダプターにデータを渡す

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
