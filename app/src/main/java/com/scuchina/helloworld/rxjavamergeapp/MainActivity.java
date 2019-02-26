package com.scuchina.helloworld.rxjavamergeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mainTextView;
    private Disposable disposable = Disposables.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindUI();
        buildObservable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
    }

    private void bindUI() {
        mainTextView = (TextView) findViewById(R.id.tv_main);
    }

    private void buildObservable() {
        this.disposable = Single.merge(buildNetworkSingle(), buildCacheSingle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showData);
    }

    private Single<Result> buildNetworkSingle() {
        return Single.just(new Result("network", 0));
    }

    private Single<Result> buildCacheSingle() {
        return Single.just(new Result("cache", 1));
    }

    private void showData(Result t) {
        System.out.println(String.format("%s %s", t.data, String.valueOf(t.ts)));
        mainTextView.setText(String.format("%s %s", t.data, String.valueOf(t.ts)));
    }
}
