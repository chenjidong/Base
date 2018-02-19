package com.kuky.base.entity

/**
 * @author Kuky
 */
class News {

    /**
     * reason : 成功的返回
     * result : {"stat":"1","data":[{"uniquekey":"a05bcd8ccb671237ece4e86bccc5ad0a","title":"上海市政协人资环建委召开专题会议 李逸平出席","date":"2018-02-09 17:02","category":"头条","author_name":"中国网","url":"http://mini.eastday.com/mobile/180209170215983.html","thumbnail_pic_s":"http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_2_mwpm_03200403.jpg","thumbnail_pic_s02":"http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_4_mwpm_03200403.jpg","thumbnail_pic_s03":"http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_1_mwpm_03200403.jpg"},{"uniquekey":"4aacb71123d9804b5cc3d16bba5ca29d","title":"【提示】沪45家超市年货礼盒大比价！最大价差202元","date":"2018-02-09 16:45","category":"头条","author_name":"东方网","url":"http://mini.eastday.com/mobile/180209164552164.html","thumbnail_pic_s":"http://04.imgmini.eastday.com/mobile/20180209/20180209164552_7d31f16ab39882477b9be45d3631dbad_3_mwpm_03200403.jpg","thumbnail_pic_s02":"http://04.imgmini.eastday.com/mobile/20180209/20180209164552_7d31f16ab39882477b9be45d3631dbad_4_mwpm_03200403.jpg","thumbnail_pic_s03":"http://04.imgmini.eastday.com/mobile/20180209/20180209164552_7d31f16ab39882477b9be45d3631dbad_5_mwpm_03200403.jpg"},{"uniquekey":"c4bb907fab7a45acb34749e983a06cc0","title":"80名小朋友齐聚市规划展览馆画出心中的美丽重庆","date":"2018-02-09 16:44","category":"头条","author_name":"大众网","url":"http://mini.eastday.com/mobile/180209164447367.html","thumbnail_pic_s":"http://02.imgmini.eastday.com/mobile/20180209/20180209164447_0bb887bfbd62a6db2fe0bc52b42bfa59_3_mwpm_03200403.jpg","thumbnail_pic_s02":"http://02.imgmini.eastday.com/mobile/20180209/20180209164447_0bb887bfbd62a6db2fe0bc52b42bfa59_2_mwpm_03200403.jpg","thumbnail_pic_s03":"http://02.imgmini.eastday.com/mobile/20180209/20180209164447_0bb887bfbd62a6db2fe0bc52b42bfa59_4_mwpm_03200403.jpg"},{"uniquekey":"9f67544be0edc68eb8d05dc1fec7d900","title":"布鲁塞尔中国文化中心\u201c欢乐春节\u201d活动启幕","date":"2018-02-09 16:36","category":"头条","author_name":"人民网","url":"http://mini.eastday.com/mobile/180209163622935.html","thumbnail_pic_s":"http://01.imgmini.eastday.com/mobile/20180209/20180209163622_964ced6e794dd5abf9dc4ec0ab81e41c_3_mwpm_03200403.jpg","thumbnail_pic_s02":"http://01.imgmini.eastday.com/mobile/20180209/20180209163622_964ced6e794dd5abf9dc4ec0ab81e41c_2_mwpm_03200403.jpg","thumbnail_pic_s03":"http://01.imgmini.eastday.com/mobile/20180209/20180209163622_964ced6e794dd5abf9dc4ec0ab81e41c_1_mwpm_03200403.jpg"}]}
     */

    var reason: String? = null
    var result: ResultBean? = null

    class ResultBean {
        /**
         * stat : 1
         * data : [{"uniquekey":"a05bcd8ccb671237ece4e86bccc5ad0a","title":"上海市政协人资环建委召开专题会议 李逸平出席","date":"2018-02-09 17:02","category":"头条","author_name":"中国网","url":"http://mini.eastday.com/mobile/180209170215983.html","thumbnail_pic_s":"http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_2_mwpm_03200403.jpg","thumbnail_pic_s02":"http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_4_mwpm_03200403.jpg","thumbnail_pic_s03":"http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_1_mwpm_03200403.jpg"},{"uniquekey":"4aacb71123d9804b5cc3d16bba5ca29d","title":"【提示】沪45家超市年货礼盒大比价！最大价差202元","date":"2018-02-09 16:45","category":"头条","author_name":"东方网","url":"http://mini.eastday.com/mobile/180209164552164.html","thumbnail_pic_s":"http://04.imgmini.eastday.com/mobile/20180209/20180209164552_7d31f16ab39882477b9be45d3631dbad_3_mwpm_03200403.jpg","thumbnail_pic_s02":"http://04.imgmini.eastday.com/mobile/20180209/20180209164552_7d31f16ab39882477b9be45d3631dbad_4_mwpm_03200403.jpg","thumbnail_pic_s03":"http://04.imgmini.eastday.com/mobile/20180209/20180209164552_7d31f16ab39882477b9be45d3631dbad_5_mwpm_03200403.jpg"},{"uniquekey":"c4bb907fab7a45acb34749e983a06cc0","title":"80名小朋友齐聚市规划展览馆画出心中的美丽重庆","date":"2018-02-09 16:44","category":"头条","author_name":"大众网","url":"http://mini.eastday.com/mobile/180209164447367.html","thumbnail_pic_s":"http://02.imgmini.eastday.com/mobile/20180209/20180209164447_0bb887bfbd62a6db2fe0bc52b42bfa59_3_mwpm_03200403.jpg","thumbnail_pic_s02":"http://02.imgmini.eastday.com/mobile/20180209/20180209164447_0bb887bfbd62a6db2fe0bc52b42bfa59_2_mwpm_03200403.jpg","thumbnail_pic_s03":"http://02.imgmini.eastday.com/mobile/20180209/20180209164447_0bb887bfbd62a6db2fe0bc52b42bfa59_4_mwpm_03200403.jpg"},{"uniquekey":"9f67544be0edc68eb8d05dc1fec7d900","title":"布鲁塞尔中国文化中心\u201c欢乐春节\u201d活动启幕","date":"2018-02-09 16:36","category":"头条","author_name":"人民网","url":"http://mini.eastday.com/mobile/180209163622935.html","thumbnail_pic_s":"http://01.imgmini.eastday.com/mobile/20180209/20180209163622_964ced6e794dd5abf9dc4ec0ab81e41c_3_mwpm_03200403.jpg","thumbnail_pic_s02":"http://01.imgmini.eastday.com/mobile/20180209/20180209163622_964ced6e794dd5abf9dc4ec0ab81e41c_2_mwpm_03200403.jpg","thumbnail_pic_s03":"http://01.imgmini.eastday.com/mobile/20180209/20180209163622_964ced6e794dd5abf9dc4ec0ab81e41c_1_mwpm_03200403.jpg"}]
         */

        var stat: String? = null
        var data: List<DataBean>? = null

        class DataBean {
            /**
             * uniquekey : a05bcd8ccb671237ece4e86bccc5ad0a
             * title : 上海市政协人资环建委召开专题会议 李逸平出席
             * date : 2018-02-09 17:02
             * category : 头条
             * author_name : 中国网
             * url : http://mini.eastday.com/mobile/180209170215983.html
             * thumbnail_pic_s : http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_2_mwpm_03200403.jpg
             * thumbnail_pic_s02 : http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_4_mwpm_03200403.jpg
             * thumbnail_pic_s03 : http://07.imgmini.eastday.com/mobile/20180209/20180209170215_367cf241fe9f5f0c9cde09688463f724_1_mwpm_03200403.jpg
             */

            var uniquekey: String? = null
            var title: String? = null
            var date: String? = null
            var category: String? = null
            var author_name: String? = null
            var url: String? = null
            var thumbnail_pic_s: String? = null
            var thumbnail_pic_s02: String? = null
            var thumbnail_pic_s03: String? = null
        }
    }
}
