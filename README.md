# Ts-Android-Library

#### 介绍
常用框架、工具相关库
ts_base：常用工具类封装
ts_glide：Glide常用方法封装
ts_permission：动态申请权限
ts_retrofit：retrofit库集成、简化使用
ts_widget：常用自定义View

#### 使用说明
1.添加 jitpack 仓库，项目的build.gradle中添加
allprojects {
    repositories {
        google()
        jcenter()
        #### maven { url 'https://www.jitpack.io' }
    }
}
2.添加类库，开发model中的build。gradle中添加（下方$version为版本号如：1.0.0）
dependencies {
        #### implementation 'com.gitee.ts_xiaoa.Ts-Android-Library:ts_base:$version'
        #### implementation 'com.gitee.ts_xiaoa.Ts-Android-Library:ts_glide:$version'
        #### implementation 'com.gitee.ts_xiaoa.Ts-Android-Library:ts_permission:$version'
        #### implementation 'com.gitee.ts_xiaoa.Ts-Android-Library:ts_retrofit:$version'
        #### implementation 'com.gitee.ts_xiaoa.Ts-Android-Library:ts_widget:$version'
}
#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 码云特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
