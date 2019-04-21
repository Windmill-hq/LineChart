# LineChart
Simple graphic representation of data changing during the time

Min API level 21

How to use?
1. Copy chartLib as module to your project 
2. In project level build.gradle file add next line 
  <code>  implementation project(':chartLib')  </code> 
3. Declare TimeLineChart view in your layout as shown below
  <code>  
    <com.contest.chart.TimeLineChart
            android:id="@+id/chart"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" /> 
   </code> 
4. Find chart view and set prepared data
  <code>  
        val chartData : LineChartData = prepareData()
        val chart: TimeLineChart = findViewById(R.id.chart)
        chart.setTitle("YOUR CHART TITLE")
        chart.setData(chartData)
          </code> 
5. Chart supports two themes Light and Dark, to switch it just call method <code>switchTheme()</code>  <br />
 on class instance as show below
    
  <code>  
        chart.switchTheme()
  </code> 
