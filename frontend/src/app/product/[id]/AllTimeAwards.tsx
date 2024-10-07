import {useLayoutEffect, useRef} from "react";
import * as am5 from "@amcharts/amcharts5";
import am5themes_Animated from "@amcharts/amcharts5/themes/Animated";
import am5xy from "@amcharts/amcharts5/xy";

const AllTimeAwards = () => {
    // 차트 그릴 DOM 요소 참조용 ref
    // useRef<HTMLDivElement>(null) 로 chartRef 가 div 요쇼임을 명시
    const chartRef = useRef<HTMLDivElement>(null);
    console.log(chartRef);

    useLayoutEffect(() => {
        // useLayoutEffect: DOM 이 완전히 준비된 이후에 차트를 초기화하기 위해 사용
        let root = am5.Root.new(chartRef.current!);
        // am5.Root.new(chartRef.current!) : 차트의 루트 요소 초기화

        // 테마설정
        const mythem = am5.Theme.new(root);
        mythem.rule("AxisLabel", ["minor"]).setAll({
            dy: 1,
        });

        mythem.rule("Grid", ["minor"]).setAll({
            strokeOpacity: 0.08,
        });

        root.setThemes([am5themes_Animated.new(root), mythem]);

        // XY 차트 생성
        let chart = root.container.children.push(
            am5xy.XYChart.new(root, {
                panX: false,
                panY: false,
                wheelX: "panX",
                wheelY: "zoomX",
                paddingLeft: 0,
            })
        );

        // 커서 추가
        let cursor = chart.set(
            "cursor",
            am5xy.XYCursor.new(root, {
                behavior: "zoomX",
            })
        );

        cursor.lineY.set("visible", false);

        // 데이터 생성 함수
        let date = new Date();
        date.setHours(0, 0, 0, 0);
        let value = 100;

        function generateData() {
            value = Math.round(Math.random() * 10 - 5 + value);
            am5.time.add(date, "day", 1);
            return {
                date: date.getTime(),
                value: value,
            }
        }

        function generatedDatas(count: number) {
            let data = [];
            for (var i = 0; i < count; ++i) {
                data.push(generateData());
            }
            return data;
        }

        // 축 생성
        let xAxis = chart.xAxes.push(
            am5xy.DateAxis.new(root, {
                // am5xy.DateAxis.new(root, {}) : 날짜를 기준으로 한 X 축 생성
                maxDeviation: 0,
                // X축 데이터 표시 균등 설정
                baseInterval: {
                    timeUnit: "day",
                    count: 1,
                },
                // X 축이 날짜 단위로 증가하도록 설정
                renderer: am5xy.AxisRendererX.new(root, {
                    // X축의 렌더러 설정
                    minorGridEnabled: true,
                    // 작은 그리드 라인 활성화
                    minGridDistance: 200,
                    minorLabelsEnabled: true,
                    // 작은 레이블 활성화
                }),
                tooltip: am5.Tooltip.new(root, {}),
            }));

        xAxis.set("minorDateFormats", {
            day: "dd",
            month: "MM",
        });

        let yAxis = chart.yAxes.push(
            am5xy.ValueAxis.new(root, {
                renderer: am5xy.AxisRendererY.new(root, {}),
            })
        );

        let series = chart.series.push(
            am5xy.LineSeries.new(root, {
                name: "Series",
                xAxis: xAxis,
                yAxis: yAxis,
                valueYField: "value",
                valueXField: "date",
                tooltip: am5.Tooltip.new(root, {
                    labelText: "{valueY}",
                }),
            })
        );

        series.bullets.push(function () {
            let bulletCircle = am5.Circle.new(root, {
                radius: 5,
                fill: series.get("fill"),
            });
            return am5.Bullet.new(root, {
                sprite: bulletCircle,
            });
        });

        chart.set(
            "scrollbarX",
            am5.Scrollbar.new(root, {
                orientation: "horizontal",
            })
        );

        let data = generatedDatas(30);
        series.data.setAll(data);

        series.appear(1000);

        return () => {
            root.dispose();
        };

    }, []);

    return <div id="chartdiv" ref={chartRef} style={{width: "100%", height: "500px"}}/>



};

export default AllTimeAwards;