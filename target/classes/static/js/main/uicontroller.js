window.UI = {
    _appList: [],
    _initFunction: [],
    start: function() {
        //
        UI.addApps([{
            "icon": "./img/icon/win10.png",
            "title": "hello"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hel你号你号啊啊你号啊啊你号啊啊啊啊你号啊啊lo"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hello"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hello"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hello"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hello"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hello"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hello"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "he你号啊啊llo"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hel你号啊啊你号啊啊lo"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "hello"
        }, {
            "icon": "./img/icon/demo.png",
            "title": "he你号啊啊啊llo"
        }]);
        UI.renderApps();
        //窗口改大小，重新渲染
        $(window).resize(function() {
            UI.renderApps();
        });
        for (var i in UI._initFunction) {
            UI._initFunction[i]();
        }
    },
    onReady: function(handle) {
        UI._initFunction.push(handle);
    },
    addApps: function(lists) {

        for (var i in lists) {
            var appAttributes = lists[i];
            var app = $("<div>", {
                class: "app",
                title: appAttributes["title"]
            });
            var icon = $("<img>", {
                class: "icon",
                src: appAttributes["icon"]
            });
            var title = $("<div>", {
                class: "title",
                text: appAttributes["title"]
            });
            app.append(icon);
            app.append(title);

            function clikcEvent(text) {
                // this.text = text;
                return function() {
                    // alert(text);
                    UI.openApp("../demo/amazeui仿微信网页版界面代码/index.html", text);
                }
            }
            app.click(clikcEvent(appAttributes["title"]));
            $("#app-list").append(app);
            UI._appList.push(app);
        }
    },
    renderApps: function() {
        var h = parseInt($("#desktop #app-list")[0].offsetHeight / 100);
        var x = 0,
            y = 0;
        for (var i in UI._appList) {
            UI._appList[i].css({
                left: x * 82 + 10,
                top: y * 100 + 10
            });
            y++;
            if (y >= h) {
                y = 0;
                x++;
            }
        }
    },
    openApp(url, title) {
        if (!title) {
            title = url;
        }
        console.log("title:" + url);
        var area, offset;

        area = ['80%', '80%'];
        var topset, leftset;
        topset = parseInt($(window).height());
        topset = (topset - (topset * 0.8)) / 2;
        leftset = parseInt($(window).width());
        leftset = (leftset - (leftset * 0.8)) / 2;
        offset = [(topset) + 'px', (leftset) + 'px'];

        var index = layer.open({
            type: 2,
            shadeClose: true,
            shade: false,
            maxmin: true, //开启最大化最小化按钮
            title: title,
            content: url,
            area: area,
            offset: offset,
            isOutAnim: false,
        });
    }
}


UI.start();