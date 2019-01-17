1、这个包包含对Android的媒体使用进行分析，列举相关的api使用事例，找出不同类的功能，进阶分析优化使用，主要有Bitmap、Canvas、Paint相关的类型api使用。
  The Canvas class holds the "draw" calls. To draw something, you need 4 basic components: A Bitmap to hold the pixels,
      a Canvas to host the draw calls (writing into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint (to describe the colors and styles for the drawing).
  这段话列出了显示信息需要的4类组件
  1）Canvas
  2）Paint
  3）Bitmap
  4）Drawable

  这四个类基本涵盖了的图像、自定义组件的绘制的基本方面，优先从Canvas的绘制开始分析。
2、Canvas类
   画布，是一种绘制时的规则:规定绘制内容时的规则和内容。
   Canvas只是绘制时的规则，但根据其规则所确定的内容实际上是要绘制在屏幕上。

 2.1 坐标系
   在Canvas中，包含Canvas坐标系与绘图坐标系两种坐标系
   Canvas坐标系
   Canvas坐标系指的是Canvas本身的坐标系，Canvas坐标系有且只有一个，且是唯一不变的，其坐标原点在View的左上角，从坐标原点向右为x轴的正半轴，从坐标原点向下为y轴的正半轴。

   绘图坐标系
   Canvas的drawXXX方法中传入的各种坐标指的都是绘图坐标系中的坐标，而非Canvas坐标系中的坐标。默认情况下，绘图坐标系与Canvas坐标系完全重合，即初始状况下，绘图坐标系的坐标原点也在View的左上角，从原点向右为x轴正半轴，从原点向下为y轴正半轴。但不同于Canvas坐标系，绘图坐标系并不是一成不变的，可以通过调用Canvas的translate方法平移坐标系，可以通过Canvas的rotate方法旋转坐标系，还可以通过Canvas的scale方法缩放坐标系，而且需要注意的是，translate、rotate、scale的操作都是基于当前绘图坐标系的，而不是基于Canvas坐标系，一旦通过以上方法对坐标系进行了操作之后，当前绘图坐标系就变化了，以后绘图都是基于更新的绘图坐标系了。也就是说，真正对我们绘图有用的是绘图坐标系而非Canvas坐标系。
