package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/12.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GoogleMapGeocodeSearchBean {

    /**
     * plus_code : {"compound_code":"G5J3+7Q Xiamen, Fujian, Chine","global_code":"7PPWG5J3+7Q"}
     * results : [{"address_components":[{"long_name":"县后","short_name":"县后","types":["establishment","point_of_interest","transit_station"]},{"long_name":"Huli Qu","short_name":"Huli Qu","types":["political","sublocality","sublocality_level_1"]},{"long_name":"Xiamen Shi","short_name":"Xiamen Shi","types":["locality","political"]},{"long_name":"Chine","short_name":"CN","types":["country","political"]}],"formatted_address":"Chine, Xiamen Shi, Huli Qu, 县后","geometry":{"location":{"lat":24.530126,"lng":118.153349},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":24.5314749802915,"lng":118.1546979802915},"southwest":{"lat":24.5287770197085,"lng":118.1520000197085}}},"place_id":"ChIJ675jtX6QFDQRhyfhqH04PFQ","plus_code":{"compound_code":"G5J3+38 Xiamen, Fujian, Chine","global_code":"7PPWG5J3+38"},"types":["establishment","point_of_interest","transit_station"]},{"address_components":[{"long_name":"Qi Shan Bei Lu","short_name":"Qi Shan Bei Lu","types":["route"]},{"long_name":"Huli Qu","short_name":"Huli Qu","types":["political","sublocality","sublocality_level_1"]},{"long_name":"Xiamen Shi","short_name":"Xiamen Shi","types":["locality","political"]},{"long_name":"Fujian Sheng","short_name":"Fujian Sheng","types":["administrative_area_level_1","political"]},{"long_name":"Chine","short_name":"CN","types":["country","political"]}],"formatted_address":"Qi Shan Bei Lu, Huli Qu, Xiamen Shi, Fujian Sheng, Chine","geometry":{"bounds":{"northeast":{"lat":24.532156,"lng":118.1542756},"southwest":{"lat":24.5316866,"lng":118.1540533}},"location":{"lat":24.5319213,"lng":118.1541645},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":24.5332702802915,"lng":118.1555134302915},"southwest":{"lat":24.5305723197085,"lng":118.1528154697085}}},"place_id":"ChIJyf9jGnyQFDQR8FdtVl_RWq4","types":["route"]},{"address_components":[{"long_name":"District de Huli","short_name":"District de Huli","types":["political","sublocality","sublocality_level_1"]},{"long_name":"Xiamen","short_name":"Xiamen","types":["locality","political"]},{"long_name":"Fujian","short_name":"Fujian","types":["administrative_area_level_1","political"]},{"long_name":"Chine","short_name":"CN","types":["country","political"]}],"formatted_address":"District de Huli, Xiamen, Fujian, Chine","geometry":{"bounds":{"northeast":{"lat":24.5618015,"lng":118.2044555},"southwest":{"lat":24.4739507,"lng":118.0721617}},"location":{"lat":24.512904,"lng":118.146769},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":24.5618015,"lng":118.2044555},"southwest":{"lat":24.4739507,"lng":118.0721617}}},"place_id":"ChIJ18uG25yaFDQRVjrGqQRF5_M","types":["political","sublocality","sublocality_level_1"]},{"address_components":[{"long_name":"Xiamen","short_name":"Xiamen","types":["locality","political"]},{"long_name":"Fujian","short_name":"Fujian","types":["administrative_area_level_1","political"]},{"long_name":"Chine","short_name":"CN","types":["country","political"]}],"formatted_address":"Xiamen, Fujian, Chine","geometry":{"bounds":{"northeast":{"lat":24.9027924,"lng":118.4147777},"southwest":{"lat":24.4183067,"lng":117.8860123}},"location":{"lat":24.479833,"lng":118.089425},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":24.9027924,"lng":118.4147777},"southwest":{"lat":24.4183067,"lng":117.8860123}}},"place_id":"ChIJJ-u_5XmDFDQRVtBolgpnoCg","types":["locality","political"]},{"address_components":[{"long_name":"Fujian","short_name":"Fujian","types":["administrative_area_level_1","political"]},{"long_name":"Chine","short_name":"CN","types":["country","political"]}],"formatted_address":"Fujian, Chine","geometry":{"bounds":{"northeast":{"lat":28.3129012,"lng":120.7273215},"southwest":{"lat":23.5285469,"lng":115.85229}},"location":{"lat":26.4836842,"lng":117.9249002},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":28.3129012,"lng":120.7273215},"southwest":{"lat":23.5285469,"lng":115.85229}}},"place_id":"ChIJ2_lEcNNUFTQR6FDb5LQ07k4","types":["administrative_area_level_1","political"]},{"address_components":[{"long_name":"Chine","short_name":"CN","types":["country","political"]}],"formatted_address":"Chine","geometry":{"bounds":{"northeast":{"lat":53.5609739,"lng":134.7754563},"southwest":{"lat":17.9996,"lng":73.4994136}},"location":{"lat":35.86166,"lng":104.195397},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":53.5609739,"lng":134.7754563},"southwest":{"lat":17.9996,"lng":73.4994136}}},"place_id":"ChIJwULG5WSOUDERbzafNHyqHZU","types":["country","political"]}]
     * status : OK
     */

    private PlusCodeBean plus_code;
    private String status;
    private List<ResultsBean> results;

    public PlusCodeBean getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(PlusCodeBean plus_code) {
        this.plus_code = plus_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class PlusCodeBean {
        /**
         * compound_code : G5J3+7Q Xiamen, Fujian, Chine
         * global_code : 7PPWG5J3+7Q
         */

        private String compound_code;
        private String global_code;

        public String getCompound_code() {
            return compound_code;
        }

        public void setCompound_code(String compound_code) {
            this.compound_code = compound_code;
        }

        public String getGlobal_code() {
            return global_code;
        }

        public void setGlobal_code(String global_code) {
            this.global_code = global_code;
        }
    }

    public static class ResultsBean {
        /**
         * address_components : [{"long_name":"县后","short_name":"县后","types":["establishment","point_of_interest","transit_station"]},{"long_name":"Huli Qu","short_name":"Huli Qu","types":["political","sublocality","sublocality_level_1"]},{"long_name":"Xiamen Shi","short_name":"Xiamen Shi","types":["locality","political"]},{"long_name":"Chine","short_name":"CN","types":["country","political"]}]
         * formatted_address : Chine, Xiamen Shi, Huli Qu, 县后
         * geometry : {"location":{"lat":24.530126,"lng":118.153349},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":24.5314749802915,"lng":118.1546979802915},"southwest":{"lat":24.5287770197085,"lng":118.1520000197085}}}
         * place_id : ChIJ675jtX6QFDQRhyfhqH04PFQ
         * plus_code : {"compound_code":"G5J3+38 Xiamen, Fujian, Chine","global_code":"7PPWG5J3+38"}
         * types : ["establishment","point_of_interest","transit_station"]
         */

        private String formatted_address;
        private GeometryBean geometry;
        private String place_id;
        private PlusCodeBeanX plus_code;
        private List<AddressComponentsBean> address_components;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public PlusCodeBeanX getPlus_code() {
            return plus_code;
        }

        public void setPlus_code(PlusCodeBeanX plus_code) {
            this.plus_code = plus_code;
        }

        public List<AddressComponentsBean> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponentsBean> address_components) {
            this.address_components = address_components;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryBean {
            /**
             * location : {"lat":24.530126,"lng":118.153349}
             * location_type : GEOMETRIC_CENTER
             * viewport : {"northeast":{"lat":24.5314749802915,"lng":118.1546979802915},"southwest":{"lat":24.5287770197085,"lng":118.1520000197085}}
             */

            private LocationBean location;
            private String location_type;
            private ViewportBean viewport;

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public String getLocation_type() {
                return location_type;
            }

            public void setLocation_type(String location_type) {
                this.location_type = location_type;
            }

            public ViewportBean getViewport() {
                return viewport;
            }

            public void setViewport(ViewportBean viewport) {
                this.viewport = viewport;
            }

            public static class LocationBean {
                /**
                 * lat : 24.530126
                 * lng : 118.153349
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class ViewportBean {
                /**
                 * northeast : {"lat":24.5314749802915,"lng":118.1546979802915}
                 * southwest : {"lat":24.5287770197085,"lng":118.1520000197085}
                 */

                private NortheastBean northeast;
                private SouthwestBean southwest;

                public NortheastBean getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBean northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBean getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBean southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBean {
                    /**
                     * lat : 24.5314749802915
                     * lng : 118.1546979802915
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBean {
                    /**
                     * lat : 24.5287770197085
                     * lng : 118.1520000197085
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }

        public static class PlusCodeBeanX {
            /**
             * compound_code : G5J3+38 Xiamen, Fujian, Chine
             * global_code : 7PPWG5J3+38
             */

            private String compound_code;
            private String global_code;

            public String getCompound_code() {
                return compound_code;
            }

            public void setCompound_code(String compound_code) {
                this.compound_code = compound_code;
            }

            public String getGlobal_code() {
                return global_code;
            }

            public void setGlobal_code(String global_code) {
                this.global_code = global_code;
            }
        }

        public static class AddressComponentsBean {
            /**
             * long_name : 县后
             * short_name : 县后
             * types : ["establishment","point_of_interest","transit_station"]
             */

            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }
        }
    }
}
