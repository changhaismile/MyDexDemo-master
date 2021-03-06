package wangyang.zun.com.mydexdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import dalvik.system.DexClassLoader;
import wangyang.zun.com.mydexdemo.dynamic.Dynamic;

public class MainActivity extends AppCompatActivity {

    private Dynamic dynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //添加一个点击事件
        findViewById(R.id.tx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDexClass();
            }
        });
    }

    /**
     * 加载dex文件中的class，并调用其中的sayHello方法
     */
    private void loadDexClass() {
        File cacheFile = FileUtils.getCacheDir(getApplicationContext());
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "dynamic_dex.jar";
        File desFile = new File(internalPath);
        try {
            if (!desFile.exists()) {
                desFile.createNewFile();
                FileUtils.copyFiles(this, "dynamic_dex.jar", desFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //下面开始加载dex class
        DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, getClassLoader());
        try {
            Class libClazz = dexClassLoader.loadClass("wangyang.zun.com.mydexdemo.dynamic.impl.DynamicImpl");
            dynamic = (Dynamic) libClazz.newInstance();
            if (dynamic != null)
                Toast.makeText(this, dynamic.sayHelloy(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
