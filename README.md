# agent
    算是项目的原型，aop功能和监控功能还没有分离，数据传输和缓存部分项目都还没建立。文档就更远了，
    现在只能自己翻翻代码，拿去改改，或者帮我写几行代码提交一下，我表示欢迎。
    项目的规划是一个java程序监控系统，是不是web都可以，监控http请求、sql执行，这些功能和现在市面
上的几个APM厂商并没有太大的差别，而且因为这规划太大了，最终能写完的多少我也不是很确定。
    
    非要说亮点，这个工具可以在运行时再对java程序进行AOP、并且随时终止AOP，终止后会回到AOP之前的状
态，然后下一次改改配置或者改几句代码后仍旧可以继续运行这个工具，接着进行AOP。没错，有些人可能已经
意识到了，Btrace，没错，就是他，用的和他的技术是类似的。但是用过Btrace脚本的人都知道，那个玩意是
不能进行统计的，也只能用来看一些问题。JVisualVM也是比较类似的工具，跟Btrace一样，只能实时看，不能
统计的，咱们不说演电影的，没有电影里一目十行代码的本领，还是需要一些工具来帮我们的，我现在写的这
个工具可以让你直接侵入代码，当然这并不是本意，只是为了实现一个监控系统而已，不建议侵入太厉害了，
毕竟生产上的东西，风险太大。

    eagleEyeAgent 的agent支持部分，提供premain和agentmain两种入口

    这个agent将提供基于JIT的aop功能，用户使用时把jar通过premain或者agentmain的方式在项目中启动，
就能针对用户想要被监控的方法进行监控了。
    用户也可以把这一部分当成aop来用，不需要依赖除了当前jar的任何jar就能对用户想要添加aop的地方编织
用户的代码到方法的第一行和方法返回前的最后一行。使用字节码重写的技术实现。ASM对需要切面的每个方法
进行字节码修改，以实现AOP。性能肯定是最快的，也没有比这对性能牺牲更小的方式了。
