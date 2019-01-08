## Relative Layout
RelativeLayout 是一个采用相对位置显示子控件的布局. 每个View的位置可以指定相对于兄弟元素(如左对齐或低于另一个视图)或位置相对于父`RelativeLayout`区域(如底部对齐,左对齐或中心对齐)。

>注意：为了获得更好的性能和工具支持,你应该与`ConstraintLayout`建立你的布局

![](../pics/relativelayout.png)
`RelativeLayout`布局在设计一个用户界面时是一个非常强大的工具，因为它可以消除视图嵌套和保持你的布局层次扁平，这有助于提高性能。如果你发现自己使用`LinearLayout`几个嵌套，你可以用一个单一的`RelativeLayout`。

#### 摆放Views
RelativeLayout允许子视图指定位置相对于父视图或兄弟元素(通过ID指定)。所以你可以通过右边界对齐两个元素，或使一个在另一个下方、屏幕居中、左居中等等。 默认情况下,所有子视图绘制在布局的左上角，所以你必须通过使用`RelativeLayout.LayoutParams`各种布局属性来定义每个视图的位置。

下面列举一些`RelativeLayout`的布局属性：
- android:layout_alignParentTop
如果为"true"，控件的顶部与父控件顶部对齐
- android:layout_centerVertical
如果为"true"，控件与父控件中心对齐
- android:layout_below
控件顶部与指定ID控件的顶部对齐
- android:layout_toRightOf
控件左边与指定ID控件的右边对齐

这里仅仅只是几个例子. 所有属性请参阅 [RelativeLayout.LayoutParams](https://developer.android.com/reference/android/widget/RelativeLayout.LayoutParams.html).

每个布局属性是一个布尔值,使布局位置相对于父`RelativeLayout`或ID引用另一个视图在视图的布局应该定位。

在XML布局、依赖与其他视图布局可以在任何顺序声明。例如,您可以声明"view1"的定位低于“view2”,即使“view2”中声明的最后一个视图层次结构。下面的例子展示了这种情况。

#### Example
控制每个视图的每个属性都是采用相对位置

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingLeft="16dp"
    android:paddingRight="16dp" >
    <EditText
        android:id="@+id/name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/reminder" />
    <Spinner
        android:id="@+id/dates"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_below="@id/name"
        android:layout_alignParentLeft="true"
        android:layout_toLeftOf="@+id/times" />
    <Spinner
        android:id="@id/times"
        android:layout_width="96dp"
        android:layout_height="wrap_content"
        android:layout_below="@id/name"
        android:layout_alignParentRight="true" />
    <Button
        android:layout_width="96dp"
        android:layout_height="wrap_content"
        android:layout_below="@id/times"
        android:layout_alignParentRight="true"
        android:text="@string/done" />
</RelativeLayout>
```
![](../pics/sample-relativelayout.png)
