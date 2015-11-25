package spider.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import spider.model.Joke;
import spider.service.JokeService;
import spider.service.JsoupService;
import common.dao.BaseDao;

@Service
public class JokeServiceImpl implements JokeService {

	private static final Logger logger = Logger.getLogger(JokeServiceImpl.class.getName());

	private static final String urlPattern = "http://xiaohua.zol.com.cn/new/%s.html";

	@Autowired
	private JsoupService jsoupService;

	public List<Joke> getJokeBy3rd(String urlIndex) throws IOException {
		List<Joke> jokes = new ArrayList<Joke>();

		String url = String.format(urlPattern, urlIndex);
		logger.info("craw url: " + url);
		Document doc = jsoupService.getDocumentFromUrl(url);
		Elements mainItems = jsoupService.getMainContentSet(doc, "ul.article-list");
		for (Element mainItem : mainItems) {
			Elements mainContents = mainItem.select("li.article-summary");
			for (Element mainContent : mainContents) {
				String title = mainContent.select("span.article-title").select("a").text();
				String text = mainContent.select("div.summary-text").text();
				String dataIdStr = mainContent.select("div[data-id]").attr("data-id");

				logger.debug(title + "-----" + text);
				Integer dataId = 0;
				try {
					dataId = Integer.parseInt(dataIdStr);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}

				Joke joke = Joke.newJoke(dataId, title, text);

				jokes.add(joke);
			}
		}

		return jokes;
	}

	@Autowired
	private BaseDao<Joke> jokeDao;

	@Transactional(rollbackFor = Exception.class)
	public int persistJoke(List<Joke> jokes) {
		int count = 0;
		for (Joke joke : jokes) {
			List<Joke> existedData = jokeDao.find("from Joke where dataId=?", new Object[] { joke.getDataId() });
			if (existedData.size() == 0) {
				jokeDao.save(joke);
				count++;
			} else {
				logger.debug("data-id=" + joke.getDataId() + " 已经存在,不再保存");
			}
		}

		return count;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Joke> getJokeFromDB(Integer page) {
		List<Joke> jokes = jokeDao.find("from Joke", new Object[] {}, page, 10);
		logger.debug(page + "--" + jokes.size());
		return jokes;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Joke getNextJoke(Joke joke) {
		Integer maxId = jokeDao.max("select max(joke.id) from Joke joke");
		Integer id = joke == null ? 0 : joke.getId();
		Joke nextJoke = null;
		while (nextJoke == null && id <= maxId) {
			id++;
			nextJoke = getJokeById(id);
		}

		return nextJoke;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Joke getJokeById(Integer id) {
		List<Joke> jokes = jokeDao.find("from Joke where id=?", new Object[] { id });
		if (jokes != null && jokes.size() > 0) {
			return jokes.get(0);
		}
		return null;
	}

}
