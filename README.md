完整的封装了通用的一套CRUD.授权等.

亮点: 简化后端查询开发量,只需要关注实体本身.以下是通用接口获取数据例子:

{
    "columns": ["fdId","fdName","fdCategory"],
    "ignoreColumns": ["fdCategory"],
    "conditions": {
        "fdCategory.id": {
            "EQUAL": "30"
        }
    },
    "isPaged": false,
    "offset": 1,
    "pageSize": 1,
    "sorts": {
        "order": "DESC"
    }
}

Swagger访问地址:
/swagger-ui/index.html
