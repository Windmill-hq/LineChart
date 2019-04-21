# LineChart
Simple graphic representation of data changing during the time

<p float="left">
   <img src="https://github.com/Windmill-hq/LineChart/blob/readme/images/day.jpg" alt="drawing" width="350"/>
   <img src="https://github.com/Windmill-hq/LineChart/blob/readme/images/night.jpg" alt="drawing" width="350"/>
</p>

# How to use?

1. Copy chartLib as module to your project 
2. In project level build.gradle file add next line  <br />
  <code>  implementation project(':chartLib')  </code> 
  
3. Declare TimeLineChart view in your layout as shown below
```xml
    <com.contest.chart.TimeLineChart
            android:id="@+id/chart"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" /> 
```

4. Find chart view and set prepared data
  ```kotlin 
        val chartData : LineChartData = prepareData()
        val chart: TimeLineChart = findViewById(R.id.chart)
        chart.setTitle("YOUR CHART TITLE")
        chart.setData(chartData)
  ```
          
5. Chart supports two themes Light and Dark, to switch it just call method <code>switchTheme()</code> on class instance <br />
  ```kotlin 
  chart.switchTheme()
 ```
 
 Min API level 21

 

