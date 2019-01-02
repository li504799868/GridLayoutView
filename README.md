# GridLayoutView
通过GridLayout实现可以定义宽度和高度的网格布局
<br />
<br />
支持网格间距：
	
	<declare-styleable name="GridLayoutView">
        <attr name="verticalSpacing" format="dimension" />
       	 <attr name="horizontalSpacing" format="dimension" />
	<attr name="dividerColor" format="color" />
    </declare-styleable>
	
<br />

使用步骤：
<br />
1、添加gradle依赖

	compile 'com.lzp:GridLayoutView:1.0.3'

2、在xml中添加GridLayoutView:
	
	<!-- 竖向 -->
	 <com.lzp.grid.GridLayoutView
          android:id="@+id/grid"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:background="@color/colorPrimaryDark"
          app:columnCount="3"
	  app:dividerColor="#50000000"
          app:horizontalSpacing="5dp"
          app:orientation="vertical"
          app:verticalSpacing="5dp" />
		  
	<!-- 横向 -->
	<com.lzp.grid.GridLayoutView
          android:id="@+id/grid"
          android:layout_width="wrap_content"
          android:layout_height="match_parent"
          android:background="@color/colorPrimaryDark"
          app:horizontalSpacing="5dp"
          app:orientation="horizontal"
          app:rowCount="3"
          app:verticalSpacing="5dp" />

请注意：竖向设置columnCount，横向需要设置rowCount，不要弄混。

3、设置Adapter适配器

	// adapter示例
	class TestAdapter(private val context: Context, private val list: List<String>) : BaseAdapter() {

		override fun getItem(position: Int): String = list[position]

		override fun getItemId(position: Int): Long = position.toLong()

		override fun getCount(): Int = list.size

		override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
			var view = convertView
			if(view == null){
				view = LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)
			}
			view!!.findViewById<TextView>(R.id.text).text = list[position]
			return view
		}

	}
	
	// 设置adapter
	GridLayoutView<String> gridView = findViewById(R.id.grid);
	gridView.setOnCellClickListener((index, s) -> Toast.makeText(this, s, Toast.LENGTH_SHORT).show());
 	gridView.setAdapter(adapter);
	
效果图
<br />
<img width="500" src="https://img-blog.csdnimg.cn/20181227144827266.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTEzMTU5NjA=,size_16,color_FFFFFF,t_70" />

<br/>
如果您觉得不错，感谢打赏一个猪蹄：

<img width=400 height=400 src="https://camo.githubusercontent.com/9a9587578e25bb3bc917c25cd772ab3ae554e4c7/68747470733a2f2f696d672d626c6f672e6373646e2e6e65742f323031383036313931383539343333343f77617465726d61726b2f322f746578742f6148523063484d364c7939696247396e4c6d4e7a5a473475626d56304c3355774d54457a4d5455354e6a413d2f666f6e742f3561364c354c32542f666f6e7473697a652f3430302f66696c6c2f49304a42516b46434d413d3d2f646973736f6c76652f3730"/>

如果在使用过程中遇到问题或者有更好的建议，欢迎发送邮件到：</br>

lzp-541@163.com
