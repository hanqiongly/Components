#当前Package处理的事RecyclerView与TabLayout之间的联动效果：
   1）TabLayout点击某个Tab，RecyclerView滑动到对应Tab所指示的那个类目的数据块中；
   2）RecyclerView滑动的过程中，TabLayout对应的Tab指示条执行滑动操作：RecyclerView上滑，则TabLayout往前一个Tab滑；
      RecyclerView下滑，则TabLayout往下一个Tab滑。
   3）顶部Tab的下拉操作，弹出完整的Tab选择器，点击之后RecyclerView滑动到对应的数据块中。