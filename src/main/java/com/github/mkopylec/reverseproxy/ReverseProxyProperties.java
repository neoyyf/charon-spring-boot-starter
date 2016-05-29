package com.github.mkopylec.reverseproxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@ConfigurationProperties("reverse-proxy")
public class ReverseProxyProperties {

	private int filterOrder = LOWEST_PRECEDENCE;
	private Timeout timeout = new Timeout();
	private List<Mapping> mappings = new ArrayList<>();

	public Timeout getTimeout() {
		return timeout;
	}

	public void setTimeout(Timeout timeout) {
		this.timeout = timeout;
	}

	public int getFilterOrder() {
		return filterOrder;
	}

	public void setFilterOrder(int filterOrder) {
		this.filterOrder = filterOrder;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}

	public void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}

	public static class Timeout {

		private int connect = 500;
		private int read = 2000;

		public int getConnect() {
			return connect;
		}

		public void setConnect(int connect) {
			this.connect = connect;
		}

		public int getRead() {
			return read;
		}

		public void setRead(int read) {
			this.read = read;
		}
	}

	public static class Mapping {

		private String path;
		private List<String> hosts;
		private boolean stripPath = true;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public List<String> getHosts() {
			return hosts;
		}

		public void setHosts(List<String> hosts) {
			this.hosts = hosts;
		}

		public boolean isStripPath() {
			return stripPath;
		}

		public void setStripPath(boolean stripPath) {
			this.stripPath = stripPath;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this)
					.append("path", path)
					.append("hosts", hosts)
					.append("stripPath", stripPath)
					.toString();
		}
	}
}
