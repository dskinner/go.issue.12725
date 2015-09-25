package cc.dasa.issue12725;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import go.client.Client;

public class MainActivity extends Activity {

  RecyclerView mRecycler;
  Adapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mRecycler = (RecyclerView) findViewById(R.id.recycler);
    mRecycler.setLayoutManager(new LinearLayoutManager(this));
    mRecycler.setItemAnimator(new DefaultItemAnimator());

    try {
      mAdapter = new Adapter(Client.Get());
      mRecycler.setAdapter(mAdapter);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  class Adapter extends RecyclerView.Adapter<ViewBinder> {

    static final int TYPE_ITEM = 0;
    static final int TYPE_AD = 1;

    final Client.Items mItems;

    public Adapter(Client.Items items) {
      mItems = items;
    }

    @Override
    public int getItemViewType(int position) {
      return (position % 2) == 0 ? TYPE_ITEM : TYPE_AD;
    }

    @Override
    public ViewBinder onCreateViewHolder(ViewGroup parent, int viewType) {
      switch (viewType) {
      case TYPE_ITEM:
        return new ItemView(parent);
      case TYPE_AD:
        return new AdView(parent);
      default:
        throw new IllegalArgumentException("Unknown view type: " + viewType);
      }
    }

    @Override
    public void onBindViewHolder(ViewBinder binder, int position) {
      binder.bind(this, position);
    }

    @Override
    public int getItemCount() {
      return (int) mItems.Len();
    }
  }

  public abstract class ViewBinder extends RecyclerView.ViewHolder {
    public ViewBinder(View itemView) {
      super(itemView);
    }

    public abstract void bind(Adapter ad, int position);
  }

  class ItemView extends ViewBinder {
    TextView mTitle;
    TextView mDescr;

    public ItemView(ViewGroup parent) {
      super(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_view, parent, false));
      mTitle = (TextView) itemView.findViewById(R.id.title);
      mDescr = (TextView) itemView.findViewById(R.id.descr);
    }

    @Override
    public void bind(Adapter ad, int position) {
      Client.Item item = ad.mItems.Get(position / 2);
      mTitle.setText(item.getTitle());
      mDescr.setText(item.getDescr());
    }
  }

  class AdView extends ViewBinder {
    public AdView(ViewGroup parent) {
      super(LayoutInflater.from(MainActivity.this).inflate(R.layout.ad_view, parent, false));
      final PublisherAdView adView = new PublisherAdView(parent.getContext());
      adView.setAdSizes(AdSize.MEDIUM_RECTANGLE);
      adView.setAdUnitId("foobar");
      adView.loadAd(new PublisherAdRequest.Builder().build());
      ((FrameLayout) itemView).addView(adView);
    }

    @Override
    public void bind(Adapter ad, int position) {
    }
  }
}
