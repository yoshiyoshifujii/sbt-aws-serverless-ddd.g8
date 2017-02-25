# sample-serverless-ddd

This project is sample of [sbt-aws-serverlss](https://github.com/yoshiyoshifujii/sbt-aws-serverless) plugin.

# Useage

## deploy prod

```sh
\$ sbt -mem 2048 \
  -DAWS_ACCOUNT_ID=<AWS Account ID> \
  -DAWS_ROLE_ARN=arn:aws:iam::<AWS Account ID>:role/<Role Name> \
  -DAWS_BUCKET_NAME=<Bucket Name>
> assembly
[success] ...
> deploy prod
ApiGateway created: <Rest Api ID>
[success] ...
```

