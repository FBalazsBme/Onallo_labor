$(function() {

    Morris.Bar({
        element: 'morris-bar-chart',
        data: [{
            y: 'user1',
            a: 100
        }, {
            y: 'user2',
            a: 75
        }, {
            y: 'user3',
            a: 50
        }, {
            y: 'user4',
            a: 75
        }, {
            y: 'user5',
            a: 50
        }, {
            y: 'user6',
            a: 75
        }],
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['Series A', 'Series B'],
        hideHover: 'auto',
        resize: true
    });
    
});
