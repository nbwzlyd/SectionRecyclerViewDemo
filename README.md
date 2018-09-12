# SectionRecyclerViewDemo
利用sectionedRecyclerViewAdapter实现分组列表的recyclerView
# 示例图片
![image](https://github.com/nbwzlyd/SectionRecyclerViewDemo/blob/master/app/src/main/res/raw/123.gif)


博客链接 https://blog.csdn.net/wzlyd1/article/details/52292548
## 前言 ##
**转载请注明出处**
http://blog.csdn.net/wzlyd1/article/details/52292548
一直在鸿洋大神的安卓群里水群，渐渐的loader和安卓弟等人都成长了起来，还记得当初他们清纯的模样。小L在群里不水了，安卓弟成长为CTO了，只有我依然默默无闻，于是决定再写博客了，之前不写，一是因为工作比较忙，二是因为我水平有限，简单的不想写，因为写了也没用，网上demo很多，难的自己也没多高的造诣，写也写不出来，所以一直都是处于“半荒废状态”，当然说到底其实还是因为懒，于是今天我再次执笔，将学到的东西全部记录下来。
## 效果 ##
先上效果图给大家看看，好有一个整体的认识

![这里写图片描述](http://img.blog.csdn.net/20160823170104563)

效果就是这样的，但是不仅仅局限于这种布局，事实上只要是三段式布局，都可以通过该demo的学习来实现，什么是三段式布局呢，就是有header -content-footer类型的布局，画一个图来解释

![这里写图片描述](http://img.blog.csdn.net/20160823171610462)
比如下面这个图就可以
![这里写图片描述](http://img.blog.csdn.net/20160823172440278)

可以看到，用途还是很广泛的，所以很需要我们去学习一下

## 怎么去实现 ##
gitbub上有一个很牛逼的类，但是貌似知道的人很少，名字叫做SectionedRecyclerViewAdapter ，地址https://github.com/truizlop/SectionedRecyclerView  但是今天我们不去研究她是怎么实现的，我们来研究他怎么用就行了

 1. 继承SectionedRecyclerViewAdapter 

```
/**
 * Created by lyd10892 on 2016/8/23.
 */

public class HotelEntityAdapter extends SectionedRecyclerViewAdapter<HeaderHolder, DescHolder, RecyclerView.ViewHolder> {

    public ArrayList<HotelEntity.TagsEntity> allTagList;
    private Context mContext;
    private LayoutInflater mInflater;
    private SparseBooleanArray mBooleanMap;//记录下哪个section是被打开的

    public HotelEntityAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBooleanMap = new SparseBooleanArray();
    }

    public void setData(ArrayList<HotelEntity.TagsEntity> allTagList) {
        this.allTagList = allTagList;
        notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        return HotelUtils.isEmpty(allTagList) ? 0 : allTagList.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int count = allTagList.get(section).tagInfoList.size();
        if (count >= 8 && !mBooleanMap.get(section)) {
            count = 8;
        }

        return HotelUtils.isEmpty(allTagList.get(section).tagInfoList) ? 0 : count;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(mInflater.inflate(R.layout.hotel_title_item, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected DescHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new DescHolder(mInflater.inflate(R.layout.hotel_desc_item, parent, false));
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderHolder holder, final int section) {
        holder.openView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOpen = mBooleanMap.get(section);
                String text = isOpen ? "展开" : "关闭";
                mBooleanMap.put(section, !isOpen);
                holder.openView.setText(text);
                notifyDataSetChanged();
            }
        });

        holder.titleView.setText(allTagList.get(section).tagsName);
        holder.openView.setText(mBooleanMap.get(section) ? "关闭" : "展开");

    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(DescHolder holder, int section, int position) {
        holder.descView.setText(allTagList.get(section).tagInfoList.get(position).tagName);

    }
}

```
这里面有几个很重要的方法也是需要我们必须重写的，是我们实现效果的关键

```
    protected abstract int getSectionCount();

    protected abstract int getItemCountForSection(int section);

    protected abstract boolean hasFooterInSection(int section);
    
    protected abstract H  onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType);

    protected abstract F  onCreateSectionFooterViewHolder(ViewGroup parent, int viewType);
    
    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);
    
    protected abstract void onBindSectionHeaderViewHolder(H holder, int section);

    protected abstract void onBindSectionFooterViewHolder(F holder, int section);
   
   protected abstract void onBindItemViewHolder(VH holder, int section, int position);
```

接下来我们详细分析这几个方法存在的具体意义
不过在说之前我们需要看一下我们的数据结构，这个也很重要

```
public class HotelEntity {
    /**
     * 要注意这个类的数据结构，很重要，直接决定了我们能不能实现分组展示
     */

    public ArrayList<TagsEntity> allTagsList;

    public class TagsEntity {
        public String tagsName;
        public ArrayList<TagInfo> tagInfoList;

        public class TagInfo {
            public String tagName;
        }
    }

}

```

**这个方法主要是用来计算我们一共有多少个section需要展示，返回值是我们最外称list的大小，在我们的示例图中，对应的为热门品牌---商业区---热门景点 等，对应的数据是我们的allTagList**
```
protected abstract int getSectionCount();
```
**这个方法是用来展示content内容区域，返回值是我们需要展示多少内容，在本例中，我们超过8条数据只展示8条，点击展开后就会展示全部数据，逻辑就在这里控制。 对应数据结构为tagInfoList**

```
protected abstract int getItemCountForSection(int section);
```

**判断是否需要底部footer布局，在该例中，我们并不需要显示footer，所以默认返回false就可以，如果你对应的section需要展示footer布局，那么就在对应的section返回true就行了**
```
protected abstract boolean hasFooterInSection(int section);

```

我们要单独说一下这个方法，这里有一个section和position ，有些人可能会弄混
section是区域，也就是我们最外层的index，position是每个section对应的内容数据的position
```
@Override
    protected void onBindItemViewHolder(DescHolder holder, int section, int position) {
        holder.descView.setText(allTagList.get(section).tagInfoList.get(position).tagName);

    }
```

至于下面的onCreateViewHolder ,onBindViewHolder不多做解释了，如果你用过recyclerView，使用方法是一样的，无非是渲染布局，绑定数据

 

 - **展示数据**
 基本上，如果上面的adapter逻辑写完，我们的布局算是完成了，首页代码如下
 

```
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HotelEntityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new HotelEntityAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this,4);//我们需要网格式的布局
        //设置header占据的空间
        manager.setSpanSizeLookup(new SectionedSpanSizeLookup(mAdapter,manager));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        HotelEntity entity = JsonUtils.analysisJsonFile(this,"json");
        mAdapter.setData(entity.allTagsList);
    }
}
```
代码里有一段很重要的注释，设置header占据的空间，没错，因为我们要仿造header的效果，我们设置的manager是GridLayoutManager，设置的每一行item数量是4，如果不重写该方法，那么header显示就会出错，核心代码如下：

```
/**
 * A SpanSizeLookup to draw section headers or footer spanning the whole width of the RecyclerView
 * when using a GridLayoutManager
 *
 * 这个类是用来自定义每个item需要占据的空间
 *
 *
 */
public class SectionedSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    protected SectionedRecyclerViewAdapter<?, ?, ?> adapter = null;
    protected GridLayoutManager layoutManager = null;

    public SectionedSpanSizeLookup(SectionedRecyclerViewAdapter<?, ?, ?> adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
    
        //header和footer占据的是全屏
        if(adapter.isSectionHeaderPosition(position) || adapter.isSectionFooterPosition(position)){
            return layoutManager.getSpanCount();
        }else{
            return 1;//其他默认1
        }

    }
}
```
最重要的是getSpanSize方法，只要是header或者是footer就返回我们设置的网格数，也就是4，代表header和footer占据4个网格的空间，其他占据1个
这样，我们就可以完美的展示我们需要的布局了
**当前我们的demo是网格布局的，你也可以设置流式布局，只需要设置不同的layoutmanager就可以了**
比如下图的效果我们也可以实现
![这里写图片描述](http://img.blog.csdn.net/20160823183738974)

核心代码已经解释完毕，当然最核心的是SectionedRecyclerViewAdapter这个类，这个类好好学习一下，会学到很多，也会实现很多app常见的布局效果，***比如设置不同的viewType展现更复杂的布局***
最后，看一下代码结构：
![这里写图片描述](http://img.blog.csdn.net/20160823183129076)
最后啰嗦一句，写博客比写代码难多了。
demo已经上传到github了，欢迎fork
https://github.com/nbwzlyd/SectionRecyclerViewDemo
