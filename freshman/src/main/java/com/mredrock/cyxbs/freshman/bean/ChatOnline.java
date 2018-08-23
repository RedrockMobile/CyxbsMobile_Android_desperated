package com.mredrock.cyxbs.freshman.bean;

import java.util.List;

public class ChatOnline {

    /**
     * array : [{"name":"重庆铜梁","array1":[{"code":"198472776","name":"重庆铜梁老乡群"}]},{"name":"重庆永川","array1":[{"code":"467050041","name":"重庆永川老乡群"}]},{"name":"重庆巫溪","array1":[{"code":"143884210","name":"重庆巫溪老乡群"}]},{"name":"重庆纂江","array1":[{"code":"109665788","name":"重庆纂江老乡群"}]},{"name":"重庆涪陵","array1":[{"code":"199748999","name":"重庆涪陵老乡群"}]},{"name":"重庆黔江","array1":[{"code":"102897346","name":"重庆黔江老乡群"}]},{"name":"重庆梁平","array1":[{"code":"85423833","name":"重庆梁平老乡群"}]},{"name":"重庆大足","array1":[{"code":"462534986","name":"重庆大足老乡群"}]},{"name":"重庆彭水","array1":[{"code":"283978475","name":"重庆彭水老乡群"}]},{"name":"重庆开州","array1":[{"code":"5657168","name":"重庆开州老乡群"}]},{"name":"重庆长寿","array1":[{"code":"594337683","name":"重庆长寿老乡群"}]},{"name":"重庆奉节","array1":[{"code":"50078959","name":"重庆奉节老乡群"}]},{"name":"重庆南川","array1":[{"code":"423494314","name":"重庆南川老乡群"}]},{"name":"重庆忠县","array1":[{"code":"115637967","name":"重庆忠县老乡群"}]},{"name":"重庆武隆","array1":[{"code":"123122421","name":"重庆武隆老乡群"}]},{"name":"重庆石柱","array1":[{"code":"289615375","name":"重庆石柱老乡群"}]},{"name":"重庆丰都","array1":[{"code":"343292119","name":"重庆丰都老乡群"}]},{"name":"重庆云阳","array1":[{"code":"118971621","name":"重庆云阳老乡群"}]},{"name":"重庆璧山","array1":[{"code":"112571803","name":"重庆璧山老乡群"}]}]
     * index : 老乡群
     */

    private String index;
    private List<ArrayBean> array;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<ArrayBean> getArray() {
        return array;
    }

    public void setArray(List<ArrayBean> array) {
        this.array = array;
    }

    public static class ArrayBean {
        /**
         * name : 重庆铜梁
         * array1 : [{"code":"198472776","name":"重庆铜梁老乡群"}]
         */

        private String name;
        private List<Array1Bean> array1;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Array1Bean> getArray1() {
            return array1;
        }

        public void setArray1(List<Array1Bean> array1) {
            this.array1 = array1;
        }

        public static class Array1Bean {
            /**
             * code : 198472776
             * name : 重庆铜梁老乡群
             */

            private String code;
            private String name;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
