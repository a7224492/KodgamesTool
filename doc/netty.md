###netty定义
- 就是对java nio封装的一个库
###netty作用
- 提供了一个现成的线程池模型，提供了一些现成的应用层协议栈代码


###关于netty线程池的一些问题
- 一个端口对应一个channel，一个channel只能对应一个NioEventLoop，也就是说，绑定一个端口只用到1个NioEventLoop，游戏服务器中好像设置了多余的情况
    - interface
- 为什么一个channel只能对应一个NioEventLoop?也就是说，为什么一个channel只能对应一个selector?
    - 如果一个channel对应多个selector，这个channel上面有accept，read，write事件时，对应的所有selector都会认为有事件就绪，多个线程就会对这个channel并发进行accept，read，write操作，如果没有一些同步操作，必然会出现并发问题，但是如果使用同步操作，不但会使编程难度增大，还会cpu的浪费，得不偿失，还不如使用一个线程去处理一个channel上面的事件
- 有了以上知识的基础，可以很好的回答一下问题
- 为什么要分boss线程池和worker线程池？
    - 单独一个线程只做接受客户端连接的任务，可以保证服务器迅速的接受客户端连接。如果这个线程不仅要接受accept事件，还要接受read,write等事件，必然会影响accept的效率
    - 让开发者可以定制worker线程池，因为worker线程池不一定和boss线程池采用同一个类型的线程池
- boss线程池和worker线程池可以合并吗？
    - 可以的，netty也提供这样的接口
    - 但是合并跟不合并相比，有以下缺点，参考上一个问题的回答
        - 合并之后，因为对于accept之后产生的新channel，netty会采用遍历所有eventloop的方式选择channel对应的eventloop，所以channel数量增加之后，accept的eventloop也会监听其他channel的read,write时间，导致accept效率降低
        - 无法定制worker线程池
- worker线程池上为什么还要加一层业务线程池？
    - 目的还是提高worker线程的效率，因为如果直接用worker线程处理业务逻辑的话，在处理一个io业务逻辑时，会导致这个线程的挂起，挂起的结果就导致这个线程监听的所有channel事件都阻塞了，降低的对应channel的read,write效率