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

        function getCharts(){

            let stageChar = echarts.init(document.getElementById("char"));

            $.ajax({
                url:"workbench/chart/getCharts.do",
                type:"get",
                dataType:"json",
                success:function (data){
                    stageChar.setOption({
                        title: {
                            text: '漏斗图',
                            subtext: '纯属虚构'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c}%"
                        },
                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        legend: {
                            data: [data.stage]
                        },

                        series: [
                            {
                                name:'漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: 100,
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
                                data: [
                                    {value: data.sum, name: data.stage},
                                ]
                            }
                        ]
                    });
                }
            })
        }

    </script>
</head>
<body>
<div id="char" style="width:600px;height: 400px"></div>

</body>
</html>
