<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>

    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script src="Echarts/echarts.min.js"></script>

    <script>

        $(function (){
            //页面加载完毕后，绘制统计图表
            getCharts();
        })

        //拿到图表数据
        function getCharts(){

            $.ajax({
                url:"workbench/chart/getCharts.do",
                type:"get",
                dataType:"json",
                success:function (data){
                    generateChart(data);
                }
            })
        };

        //生成漏斗图
        function generateChart(data){

            let stageChar = echarts.init(document.getElementById("char"));

            let option = {
                title: {
                    text: '交易统计图',
                    subtext: '统计交易阶段适量的漏斗图'
                },
                series: [
                    {
                        name:'交易统计图',
                        type:'funnel',
                        left: '10%',
                        top: 60,
                        //x2: 80,
                        bottom: 60,
                        width: '80%',
                        // height: {totalHeight} - y - y2,
                        min: 0,
                        max: data.total,
                        minSize: '0%',
                        maxSize: '100%',
                        sort: 'descending',
                        gap: 2,
                        label: {
                            show: true,
                            position: 'inside'
                        },
                        labelLine: {
                            length: 10,
                            lineStyle: {
                                width: 1,
                                type: 'solid'
                            }
                        },
                        itemStyle: {
                            borderColor: '#fff',
                            borderWidth: 1
                        },
                        emphasis: {
                            label: {
                                fontSize: 20
                            }
                        },
                        data: data.dataList
                    }
                ]
            };

            stageChar.setOption(option);
        }



    </script>
</head>
<body>
<div id="char" style="width:600px;height: 400px"></div>

</body>
</html>
