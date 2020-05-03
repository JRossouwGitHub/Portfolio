package studio.crunchyiee.degreeprogrammemapper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<ScreenItem> mListScreen;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen,null);

        ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
        TextView title = layoutScreen.findViewById(R.id.intro_title);
        Button overlay_btn = layoutScreen.findViewById(R.id.overlay_btn);

        title.setText(mListScreen.get(position).getTitle());
        title.setText(mListScreen.get(position).getTitle());
        imgSlide.setImageResource(mListScreen.get(position).getScreenImg());

        overlay_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              /*  Intent selectionActivity = new Intent(mContext.getApplicationContext(), ModulesPage.class);
                mContext.startActivity(selectionActivity);*/

                //Go to Modules Page and give Pathway as Extra
                Intent modulesPage = new Intent(mContext.getApplicationContext(), ModulesPage.class);
                modulesPage.putExtra("pathway", mListScreen.get(position).getTitle());
                mContext.startActivity(modulesPage);


            }
        });

        container.addView(layoutScreen);

        return layoutScreen;

    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
