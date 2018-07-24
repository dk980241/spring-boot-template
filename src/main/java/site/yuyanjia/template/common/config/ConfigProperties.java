package site.yuyanjia.template.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置资源
 *
 * @author seer
 * @date 2018/7/24 14:38
 */
@ConfigurationProperties(value = ConfigProperties.PREFIX)
public class ConfigProperties {
    public static final String PREFIX = "yuyanjia";

    private Tomcat tomcat;

    public Tomcat getTomcat() {
        return tomcat;
    }

    public void setTomcat(Tomcat tomcat) {
        this.tomcat = tomcat;
    }

    /**
     * Tomcat配置文件
     *
     * @author seer
     * @date 2018/7/24 14:45
     */
    public static class Tomcat {
        /**
         * 连接超时，单位ms
         */
        private Integer connectionTimeout = 20000;

        /**
         * 接收连接线程数量，参考cpu核数
         */
        private Integer acceptorThreadCount = 2;

        /**
         * 最小监听线程
         */
        private Integer minSpareThreads = 5;

        /**
         * 最大监听线程
         * 同时相应客户请求最大值
         */
        private Integer maxSpareThreads = 200;

        /**
         * 最大排队数
         */
        private Integer acceptCount = 200;

        /**
         * 最大连接数
         */
        private Integer maxConnections = 800;

        /**
         * 最大线程数
         */
        private Integer maxThreads = 500;

        /**
         * 运行模式
         */
        private String protocol = "org.apache.coyote.http11.Http11NioProtocol";

        public Integer getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public Integer getAcceptorThreadCount() {
            return acceptorThreadCount;
        }

        public void setAcceptorThreadCount(Integer acceptorThreadCount) {
            this.acceptorThreadCount = acceptorThreadCount;
        }

        public Integer getMinSpareThreads() {
            return minSpareThreads;
        }

        public void setMinSpareThreads(Integer minSpareThreads) {
            this.minSpareThreads = minSpareThreads;
        }

        public Integer getMaxSpareThreads() {
            return maxSpareThreads;
        }

        public void setMaxSpareThreads(Integer maxSpareThreads) {
            this.maxSpareThreads = maxSpareThreads;
        }

        public Integer getAcceptCount() {
            return acceptCount;
        }

        public void setAcceptCount(Integer acceptCount) {
            this.acceptCount = acceptCount;
        }

        public Integer getMaxConnections() {
            return maxConnections;
        }

        public void setMaxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
        }

        public Integer getMaxThreads() {
            return maxThreads;
        }

        public void setMaxThreads(Integer maxThreads) {
            this.maxThreads = maxThreads;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }
    }

}
