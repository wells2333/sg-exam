import Fuck from './wxParse'

const wxParse = Component({
    properties: {
        html: {
            type: String,
            value: '',
        },
    },
    observers: {
        html: function () {
            Fuck.wxParse('wxParseData', 'html', this.properties.html, this)
        },
    },
});

export default wxParse;