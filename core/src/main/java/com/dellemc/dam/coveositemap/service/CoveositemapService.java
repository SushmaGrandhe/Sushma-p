package com.dellemc.dam.coveositemap.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class CoveositemapService {

	private static Logger LOG = LoggerFactory.getLogger(CoveositemapService.class);

	private static final String IMAGES_ROOT_PATH = "/content/dam/images";
	private static final String DV2_PATH = "/content/dam/documents-and-videos/dv2";
	private static final String SS2_PATH = "/content/dam/ss2";
	private static final String SS1_PATH = "/content/dam/ss1";
	private static final String DCD_PATH = "/content/dam/dynamic-content-delivery";
	private static final String TAH_PATH = "/content/dam/training-and-help";

	// isImagePath predicate
	private static final Predicate<CoveoDAMResult> imgPathPredicate = coveoDAMResult -> coveoDAMResult.getPath()
			.contains(IMAGES_ROOT_PATH) ? Boolean.TRUE : Boolean.FALSE;
	private static final Predicate<CoveoDAMResult> dv2PathPredicate = coveoDAMResult -> coveoDAMResult.getPath()
			.contains(DV2_PATH) ? Boolean.TRUE : Boolean.FALSE;

	private static final Predicate<CoveoDAMResult> ss2PathPredicate = coveoDAMResult -> coveoDAMResult.getPath()
			.contains(SS2_PATH) ? Boolean.TRUE : Boolean.FALSE;

	private static final Predicate<CoveoDAMResult> ss1PathPredicate = coveoDAMResult -> coveoDAMResult.getPath()
			.contains(SS1_PATH) ? Boolean.TRUE : Boolean.FALSE;

	private static final Predicate<CoveoDAMResult> dcdPathPredicate = coveoDAMResult -> coveoDAMResult.getPath()
			.contains(DCD_PATH) ? Boolean.TRUE : Boolean.FALSE;

	private static final Predicate<CoveoDAMResult> tahPathPredicate = coveoDAMResult -> coveoDAMResult.getPath()
			.contains(TAH_PATH) ? Boolean.TRUE : Boolean.FALSE;

	private final Predicate<CoveoDAMResult> docAssetPredicate = coveoDAMResult -> isDocExtensionAsset(
			coveoDAMResult.getPath());

	public CoveositemapService() {

	}

	public CoveositemapService(String domain, int limit, String coveoapi) {
		this.domain = domain;
		this.limit = limit;
		this.coveoapi = coveoapi;
		this.appDomain = (String) this.coveoapi.subSequence(0,
				this.coveoapi.indexOf("/assetservices/api/coveo/assets/search"));
		if (null != this.appDomain && this.appDomain.equalsIgnoreCase("https://dam.delltechnologies.com")) {
			this.appDomain = "https://origin-www.emc.com";
		}
		if (null != this.appDomain && (this.appDomain.equalsIgnoreCase("https://qa-dam.delltechnologies.com") || 
		this.appDomain.equalsIgnoreCase("https://qa1-dam.delltechnologies.com"))) {
			this.appDomain = "https://qa-lb.delltechnologies.com";
		}


	}

	private String domain;

	private String appDomain;

	private int limit;

	private String coveoapi;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getExtensionByApacheCommonLib(String filename) {
		return FilenameUtils.getExtension(filename);
	}

	public Boolean isDocExtensionAsset(String fileName) {

		String fileExtension = getExtensionByApacheCommonLib(fileName);
		return fileExtension.contains("pdf") ||
				fileExtension.contains("doc") ||
				fileExtension.contains("xls") ||
				fileExtension.contains("ppt");

	}

	public String getAppDomain() {
		return appDomain;
	}

	public SitemapIndex generateSiteMapIndex(String env, Long records) {
		SitemapIndex sitemapIndex = new SitemapIndex();
		if (records != 0) {
			Long entries = records / limit;
			for (int i = 0; i < entries + 1; i++) {
				String url;
				url = domain + "/sitemap-assets-" + env + "-" + Integer.toString(i + 1) + ".xml";
				Sitemap sitemap = new Sitemap();
				sitemap.setLoc(url);
				sitemapIndex.addSitemap(sitemap);
			}
			return sitemapIndex;
		} else
			return null;
	}



	public Urlset generateSitemapTest(List<CoveoDAMResult> coveoDAMResultList, String env) {

		Urlset urlset = new Urlset();
		for (CoveoDAMResult coveoDAMResult : coveoDAMResultList) {
			Url url = new Url();
			url.setLastmod(coveoDAMResult.getLastModified());
			url.setLoc(this.getAppDomain() + coveoDAMResult.getPath());
			urlset.addUrl(url);
		}
		return urlset;
	}

	public Urlset generateSitemapUAT(List<CoveoDAMResult> coveoDAMResultList, String env,
			CopyOnWriteArraySet<String> assetPaths)
			throws RuntimeException {

		Urlset urlset = new Urlset();

		if (env.contains("images")) {
			urlset.addUrls(coveoDAMResultList.stream()
					.parallel()
					.unordered()
					.filter(imgPathPredicate.and(docAssetPredicate.negate()))
					.map(coveoDAMResult -> mapDAMResulToURL(coveoDAMResult, assetPaths))
					.limit(limit)
					.collect(Collectors.toList()));
		}
		if (env.contains("dv2")) {
			urlset.addUrls(coveoDAMResultList.stream()
					.parallel()
					.unordered()
					.filter(dv2PathPredicate.and(docAssetPredicate.negate()))
					.map(coveoDAMResult -> mapDAMResulToURL(coveoDAMResult, assetPaths))
					.limit(limit)
					.collect(Collectors.toList()));
		}
		if (env.contains("ss2")) {
			urlset.addUrls(coveoDAMResultList.stream()
					.parallel()
					.unordered()
					.filter(ss2PathPredicate.and(docAssetPredicate.negate()))
					.map(coveoDAMResult -> mapDAMResulToURL(coveoDAMResult, assetPaths))
					.limit(limit)
					.collect(Collectors.toList()));
		}
		if (env.contains("doc")) {
			urlset.addUrls(coveoDAMResultList.stream()
					.parallel()
					.unordered()
					.filter(docAssetPredicate)
					.map(coveoDAMResult -> mapDAMResulToURL(coveoDAMResult, assetPaths))
					.limit(limit)
					.collect(Collectors.toList()));
		}

		if (!env.contains("images") &&
				!env.contains("dv2") &&
				!env.contains("ss2") &&
				!env.contains("doc")) {
			urlset.addUrls(coveoDAMResultList.stream()
					.parallel()
					.unordered()
					.filter((ss1PathPredicate.or(dcdPathPredicate).or(tahPathPredicate))
							.and(docAssetPredicate.negate()))
					.map(coveoDAMResult -> mapDAMResulToURL(coveoDAMResult, assetPaths))
					.limit(limit)
					.collect(Collectors.toList()));
		}

		return urlset;

	}

	private Url mapDAMResulToURL(CoveoDAMResult coveoDAMResult, CopyOnWriteArraySet<String> assetPaths) {

		if (null != assetPaths &&
				assetPaths.stream()
						.parallel()
						.anyMatch(assetPath -> assetPath.contains(coveoDAMResult.getPath()))) {
			return new Url(this.getAppDomain() + coveoDAMResult.getPath(),
					LocalDateTime.now()
							.truncatedTo(ChronoUnit.SECONDS)
							.format(DateTimeFormatter.ISO_DATE_TIME));

		} else {
			return new Url(this.getAppDomain() + coveoDAMResult.getPath(), coveoDAMResult.getLastModified());
		}
	}

}