Project Overview

Phase 1
//implement the dynamo DB large objects pattern
//receives S3 new file event (sqs > lambda)
//adds metadata to dynamo table (sqs > dynamo ??)

Phase 2
//client will read the metadata from dynamoDB (api gateway)
//retrieve data from S3
//set up aws CI/CD 
