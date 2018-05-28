package com.jaaziel.irc.etherfi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity {

    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = getTabHost();

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("ether");
        tabSpec1.setIndicator("Ethernet");
        Intent intent1 = new Intent(this, EthernetMain.class);
        tabSpec1.setContent(intent1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("wi");
        tabSpec2.setIndicator("WiFi");
        Intent intent2 = new Intent(this, VistaWifi.class);
        tabSpec2.setContent(intent2);

        tabHost.addTab(tabSpec1);
        tabHost.addTab(tabSpec2);
    }
}
