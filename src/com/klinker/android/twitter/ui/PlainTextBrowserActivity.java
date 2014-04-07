package com.klinker.android.twitter.ui;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.klinker.android.twitter.R;
import com.klinker.android.twitter.manipulations.widgets.HoloTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by luke on 4/2/14.
 */
public class PlainTextBrowserActivity extends BrowserActivity {

    HoloTextView webText;
    ScrollView scrollView;
    LinearLayout spinner;

    @Override
    public void setUpLayout() {
        setContentView(R.layout.mobilized_fragment);
        webText = (HoloTextView) findViewById(R.id.webpage_text);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        spinner = (LinearLayout) findViewById(R.id.spinner);

        getTextFromSite();
    }

    public void getTextFromSite() {
        Thread getText = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();

                    String text = "";
                    String title = doc.title();

                    if(doc != null) {
                        Elements paragraphs = doc.getElementsByTag("p");

                        if (paragraphs.hasText()) {
                            text = paragraphs.html();
                        }
                    }

                    final String article =
                            "<strong><big>" + title + "</big></strong>" +
                                    "<br/><br/>" +
                                    text.replaceAll("<img.+?>", "") +
                                    "<br/>"; // one space at the bottom to make it look nicer

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webText.setText(Html.fromHtml(article.replaceAll("\\n\\n\\n", "<br/><br/>")
                                    .replaceAll("\\n\\n", "<br/><br/>")
                                    .replaceAll("\\n", "<br/><br/>")));
                            webText.setMovementMethod(LinkMovementMethod.getInstance());
                            webText.setTextSize(settings.textSize);

                            spinner.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webText.setText(getResources().getString(R.string.error_loading_page));
                        }
                    });
                }
            }
        });

        getText.setPriority(8);
        getText.start();
    }
}