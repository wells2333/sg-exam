export default defineAppConfig({
    pages: [
        'pages/home/index',
        'pages/index/index',
        'pages/register/index',
        'pages/forgotpassowrd/index',
        'pages/subjects/index',
        'pages/user/index'
    ],
    subpackages: [{
        'root': 'pages/exam_pages/',
        'pages': [
            'exam/index',
            'exam_detail/index',
            'course/index',
            'course_detail/index',
            'course_section/index',
            'subjects_list/index',
            'subjects_detail/index',
            'all_subject/index',
            'next_subject/index',
            'record/index',
            'answer/index',
            'favorite/index',
        ]
    }, {
        'root': 'pages/user_pages/',
        'pages': [
            'user_info/index',
            'messages/index',
            'message_detail/index',
            'webview/index',
            'about/index',
            'share/index',
            'phone_info/index'
        ]
    }],
    window: {
        backgroundTextStyle: 'light',
        navigationBarBackgroundColor: '#fff',
        navigationBarTitleText: 'SG-EXAM',
        navigationBarTextStyle: 'black'
    },
    tabBar: {
        color: "#505050",
        selectedColor: "#3c4a54",
        backgroundColor: "#ffffff",
        borderStyle: "black",
        position: "bottom",
        list: [
            {
                "pagePath": "pages/home/index",
                "text": "首页",
                "iconPath": "assert/home.png",
                "selectedIconPath": "assert/home_selected.png"
            },
            {
                "pagePath": "pages/subjects/index",
                "text": "题库",
                "iconPath": "assert/subjects.png",
                "selectedIconPath": "assert/subjects_selected.png"
            },
            {
                "pagePath": "pages/user/index",
                "text": "我的",
                "iconPath": "assert/user.png",
                "selectedIconPath": "assert/user_selected.png"
            }
        ]
    },
    usingComponents: {}
})
