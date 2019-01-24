## FileProvider文件共享
`FileProvider`是`ContentProvider`的一个特殊的子类，通过创建一个`content://Uri`而不是`file:///Uri`促进应用程序之间安全的文件共享。

一个内容URI允许您临时授予读和写访问权限。当您创建一个包含URI的Intent，为了内容的URI发送给客户端,你还可以调用`Intent.setFlags()`添加权限。这个些权限只要这个接收Intent的Activity所在的栈还是活跃的就有效。对于接收Intent的Service，只要Service在运行授权就有效。

相比之下，控制`file:///Uri`访问文件，你必须修改底层文件的文件系统的权限。您提供的权限对任何应用程序都是可用，并保持有效,直到你改变他们。这种级别的访问从根本上是不安全的。

增加的文件访问安全级别内容提供的URI使`FileProvider`成为Android的安全基础设施的重要组成部分。

`FileProvider`大概包含一下几个主题:
- 定义一个`FileProvider`
- 指定分享的文件
- 生成文件的URI
- 授予URI的临时访问权限
- 为其他应用提供URI



###定义一个`FileProvider`

由于FileProvider默认提供的功能包括内容URI生成的文件，你不需要在代码中定义一个子类。相反，你可以在你的应用的XML中直接使用`FileProvider`。指定FileProvider组件需要在AndroidMenifest添加`<provider>`标签。
- 将`android:name`设置为`android:android.support.v4.content.FileProvider`
- 将`android:authorities`属性设置为一个你可以控制的域；例如你可以控制域名`mydomain.com`，你可以使用`com.mydomain.fileprovider`。
- 将`android:exported`属性设置为`false`，因为`FileProvider`不需要公开。
- 将`android:grantUriPermissions`属性设置为`true`，可以授予临时访问权限。

例如：
```xml
<manifest>
    ...
    <application>
        ...
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="com.mydomain.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            ...
        </provider>
        ...
    </application>
</manifest>
```
如果你想覆盖`FileProvider`任何方法的默认行为，扩展FileProvider类和在`<provider>`使用`android:name`指定完全限定类名。

### 指定分享的文件
FileProvider只能生成一个内容文件的URI事先在您指定的目录中。指定一个目录,使用`<paths>`元素的子元素在XML指定其存储区域和路径。例如,下面的使用`<paths>`元素的子元素告诉`FileProvider`你打算uri请求内容的`images/`子目录你的私人文件。
```xml
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <files-path name="my_images" path="images/"/>
    ...
</paths>
```
`<paths>`必须包含以下一个或多个元素:

```xml
<files-path name="name" path="path" />`
```
Represents files in the files/ subdirectory of your app's internal storage area. This subdirectory is the same as the value returned by Context.getFilesDir().

```xml
<cache-path name="name" path="path" />
```
Represents files in the cache subdirectory of your app's internal storage area. The root path of this subdirectory is the same as the value returned by getCacheDir().

```xml
<external-path name="name" path="path" />
```
Represents files in the root of the external storage area. The root path of this subdirectory is the same as the value returned by Environment.getExternalStorageDirectory().

```xml
<external-files-path name="name" path="path" />
```
Represents files in the root of your app's external storage area. The root path of this subdirectory is the same as the value returned by Context#getExternalFilesDir(String) Context.getExternalFilesDir(null).

```xml
<external-cache-path name="name" path="path" />
```
Represents files in the root of your app's external cache area. The root path of this subdirectory is the same as the value returned by Context.getExternalCacheDir().

```xml
<external-media-path name="name" path="path" />
```
Represents files in the root of your app's external media area. The root path of this subdirectory is the same as the value returned by the first result of Context.getExternalMediaDirs().
>注意: 这个目录仅在API 21+的设备上有效

这些子元素都可以使用以下的属性：

- `name="name"`
一个URI路径段。为了保证安全,这个值隐藏你共享的子目录的名称。这个值的子目录名称包含在`path`属性。
- `path="path"`
你共享的子目录。虽然`name`属性是一个URI路径,`path`的值是一个真实的子目录的名称。注意,这个值是指向一个子目录,而不是单个文件。你不能共享单个文件的文件名,也不可以使用通配符指定文件的一个子集。

您必须为您想要的内容uri的每个目录所包含的文件指定一个`<paths>`的子元素。例如,这些XML元素指定两个目录:
```xml
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <files-path name="my_images" path="images/"/>
    <files-path name="my_docs" path="docs/"/>
</paths>
```
把`<paths>`元素和它的子元素放在您的项目中一个XML文件。例如,您可以将它们添加到一个名为`res/xml/file_paths.xml`的新文件。在定义了`FileProvider`的`<provider>`元素中添加一个`<meta-data>`子元素，将这个文件与`FileProvider`连接起来。 将`<meta-data>`元素的 `"android:name"`属性设置为`android.support.FILE_PROVIDER_PATHS`. 将元素的 `"android:resource"`属性设置为`@xml/file_paths` (注意不需要指定`.xml`后缀). 例如：
```xml
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="com.mydomain.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

### 生成文件的URI
为了通过URI分享文件给其他应用, 你的应用必须生成一个URI. 首先为文件创建一个File对象，传给`getUriForFile()`. 然后将`getUriForFile()`返回的URI添加到Intent中发送给其他应用。 接收改URI的应用可以打开这个文件，并通过调用`ContentResolver.openFileDescriptor`获取一个` ParcelFileDescriptor`来访问其内容。

例如, 假设你的应用通过`FileProvider`提供一个文件给另一个应用， 属性`authority`值为 `com.mydomain.fileprovider`. 为了得到你的内部存储子目录`images/`目录下面`default_image.jpg`图片的URI，添加下面的代码:
```java
File imagePath = new File(Context.getFilesDir(), "images");
File newFile = new File(imagePath, "default_image.jpg");
Uri contentUri = getUriForFile(getContext(), "com.mydomain.fileprovider", newFile);
```
上面的代码片段中，`getUriForFile()`返回URI `content://com.mydomain.fileprovider/my_images/default_image.jpg`.

>**注意：为防止与依赖库中的`android.support.v4.content.FileProvider`冲突，尽量自定义一个FileProvider，即使不覆写任何方法。**


### 授予URI的临时权限
为了授予`getUriForFile()`返回的URI访问权限, 需要做以下几项中的一项:
- 为`content://`Uri调用方法`Context.grantUriPermission(package, Uri, mode_flags)`使用所需的模式标志。这样为指定的package授予临时的访问权限，该权限由参数`mode_flags`，该参数的值可以为`FLAG_GRANT_READ_URI_PERMISSION`, `FLAG_GRANT_WRITE_URI_PERMISSION` 或者两个都设置. 这个权限的有效期至调用方法`revokeUriPermission()`或者到设备重启。
- 调用setData()时将内容URI放入Intent.
- 其次，调用方法`Intent.setFlags()` 设置`FLAG_GRANT_READ_URI_PERMISSION`或 `FLAG_GRANT_WRITE_URI_PERMISSION`或者两个都设置.
- 最后, 发送Intent到其他应用. 通常来说, 你是通过调用`setResult()`实现.
只要接收的Activity所在的栈还活跃，通过Intent授予的权限就有效。当该栈结束了，权限将自动被移除。权限授予到另一个应用后，该权限将自动拓展到该应用的其他组件。

### 为其他应用提供URI
有几种方式为其他应用提供文件内容URI. 其中最常用的一种方式就是其他应用调用`startActivityForResult ()`启动你的应用中的`Activity`时，将内容URI放到Intent作为结果给调用者. 在后一种情况下，一旦用户选取了你的应用提供的内容URI. 在这两种情况下，你的应用通过`setResult()`返回内容URI.

你也可以将内容URI放入一个`ClipData`对象中，并将该对象加入到要发送给其他应用的Intent中。为此，调用`Intent.setClipData()`。 当你使用这个方法，你可以添加多个`ClipData`对象到Intent，每个都有自己的内容URI。当你调用`Intent.setFlags()`设置临时访问权限时，该intent所有的URI都拥有相同的权限。

>注意: `Intent.setClipData()`方法只在API16 (Android 4.1)及以上才能使用。如果你想兼容之前的老版本，你应该每次发送一个内容URI。把`action`设置为`ACTION_SEND`并调用`setData()`将URI放入数据。

### 更多内容
学习FileProvider的更多相关内容, 参见Android的培训课程[使用URIs安全地分析文件](https://developer.android.com/training/secure-file-sharing/index.html).



-------------
[FileProvider](https://developer.android.com/reference/android/support/v4/content/FileProvider)
