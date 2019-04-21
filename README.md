# LineChart
Simple graphic representation of data changing during the time

<p float="left">
   <img src="https://github.com/Windmill-hq/LineChart/blob/develop/images/day.jpg" alt="drawing" width="350"/>
   <img src="https://github.com/Windmill-hq/LineChart/blob/develop/images/night.jpg" alt="drawing" width="350"/>
</p>

<img src="https://github.com/Windmill-hq/LineChart/blob/develop/images/demo.gif" width="350" height="350" />

# How to use?

1. Copy ```chartLib``` as module to your project 
2.  In  ```settings.gradle``` file add library as module ```':chartLib'```, so the file should look like   <br />
``` 
  include ':chartLib', ':YOUR_APP_NAME'
```
3. In module level ```build.gradle``` file add next line to block ```dependencies```  <br />
  ```
  dependencies{
      implementation project(':chartLib')
  }
  ```
  
4. Declare TimeLineChart view in your layout as shown below
```xml
    <com.contest.chart.TimeLineChart
            android:id="@+id/chart"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content" /> 
```

5. Find chart view and set prepared data
  ```kotlin 
        val chartData : LineChartData = prepareData()
        val chart: TimeLineChart = findViewById(R.id.chart)
        chart.setTitle("YOUR CHART TITLE")
        chart.setData(chartData)
  ```
          
6. Chart supports  Light and Dark themes, to switch between them just call method <code>switchTheme()</code> on class instance <br />
  ```kotlin 
  chart.switchTheme()
 ```
 
7. In order to represent your data, chart requires you to provide data as instance of class <code>LineChartData </code> <br/>
  ```kotlin 
  class LineChartData(val id: Int) {
    val brokenLines = ArrayList<BrokenLine>()
    lateinit var timeLine: LongArray
}
  ```
  Field ```timeLine``` is array of <code>long</code> that represented time line during wich your data changes.<br/>
Since chart can represent a few chart lines simultaneously in one view with the same time frame, you need to provide your data as instance of class ```BrokenLine``` and add it to  ```brokenLines``` list. 

Class ```BrokenLine``` declaration
```kotlin 
class BrokenLine(val points: FloatArray, val name: String, val color: String)
```
where: <br />
   ```points``` - array of values of your data <br />
   ```name``` - name of data <br />
   ```color``` - color in HEX format  <br />
   
   IMPORTANT: size of  ```points``` and ```timeLine``` arrays should be always equal 

8.  Min API level 21

