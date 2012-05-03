
declare variable $params as xs:string external;

let $paramXml := xdmp:unquote($params)
let $function := xdmp:function(xs:QName(xs:string($paramXml/method/@name)))
let $p := $paramXml/method/params/param/text()          
return 
(    
    xdmp:eval(fn:concat('declare variable $function external; xdmp:apply($function,',fn:string-join($p,","),')'),(xs:QName('function'),$function))    
)
