ev-aikau-auditreport
======================
This addon from EisenVault helps to view the audit data for a document on a separate page built using AIKAU framework.

![ev-aikau-auditreport](https://cloud.githubusercontent.com/assets/3936714/9897648/a189966e-5c7d-11e5-94ec-c608de6f5e89.png)

A custom Document Library Action and link on Document Details page are provided for viewing the audit report for a document.

![document library action](https://cloud.githubusercontent.com/assets/3936714/9900992/2e42caa8-5c97-11e5-9f7b-b3210bd2cbe8.png)

![document details action](https://cloud.githubusercontent.com/assets/3936714/9901010/5510babe-5c97-11e5-9dcc-2ba955e2a10c.png)  


EisenVault [www.eisenvault.com] takes away the hassle of managing documents by storing them in both electronic and physical forms, enabling smart-search and deploying quick retrieval measures. Our innovative cloud-based document management system can be used in multiple industry sectors, and caters to specific requirements of various job roles. Our vision - to be your preferred partner for electronic and physical document management.

# API Endpoints

This addon provides the following REST API endpoints for audit functionality:

## 1. Node Audit Trail API

**Endpoint:** `GET /ev/nodeaudittrail`

**Description:** Retrieves the complete audit trail for a specific node/document.

**Parameters:**
- `nodeRef` (required): The node reference of the document in Alfresco format (e.g., `workspace://SpacesStore/12345678-1234-1234-1234-123456789012`)

**Response Format:**
```json
{
  "data": [
    {
      "userName": "admin",
      "applicationName": "alfresco-access",
      "applicationMethod": "UPDATE NODE PROPERTIES",
      "date": "2023-12-01T10:30:00.000Z",
      "auditValues": {
        "/alfresco-access/transaction/action": "updateNodeProperties",
        "/alfresco-access/transaction/path": "/app:company_home/cm:test-document.pdf",
        "/alfresco-access/transaction/properties/cm:name": "test-document.pdf"
      }
    }
  ],
  "nodeRef": "12345678-1234-1234-1234-123456789012",
  "fileName": "test-document.pdf",
  "count": 1,
  "returnStatus": true,
  "statusMessage": "Successfully retrieved audit trail for nodeRef[12345678-1234-1234-1234-123456789012]"
}
```

**Response Fields:**
- `data`: Array of audit entries
  - `userName`: Username who performed the action
  - `applicationName`: Application that generated the audit entry (typically "alfresco-access")
  - `applicationMethod`: Human-readable description of the action performed
  - `date`: Timestamp of the audit entry
  - `auditValues`: Key-value pairs containing detailed audit information
- `nodeRef`: The node reference ID
- `fileName`: Name of the document
- `count`: Total number of audit entries
- `returnStatus`: Boolean indicating success/failure
- `statusMessage`: Descriptive message about the operation

**Error Response:**
```json
{
  "returnStatus": false,
  "statusMessage": "Error message describing the issue"
}
```

## 2. Permission Check API

**Endpoint:** `GET /ev/hasPermissions`

**Description:** Checks if the current user has permission to view audit data for a specific node.

**Parameters:**
- `nodeRef` (required): The node reference of the document in Alfresco format

**Response Format:**
```json
{
  "result": true
}
```

**Response Fields:**
- `result`: Boolean indicating whether the user has permission to view audit data

**Error Response:**
```json
{
  "result": false
}
```

## 3. Document Details Audit Component

**Endpoint:** `GET /eisenvault/components/document-details/evaudit`

**Description:** Returns the HTML component for displaying audit information in the document details panel.

**Parameters:** None (uses context from the current document)

**Response:** HTML template for the audit component

# Configuration

This addon uses the default **alfresco-access** audit application. Make sure that you have below lines added to alfresco-global.properties:
  ```
  audit.enabled=true
  audit.alfresco-access.enabled=true
  audit.alfresco-access.sub-actions.enabled=true
  ```

## Access configuration (optional)

The following configuration shows the default permission configuration. It allows anybody show audit information.
  ```
  # Comma-separated list of alfresco groups that may use the audit report service. 
  # The 'ALL' setting allows all users to use it. The opposite to allow for nobody is an empty setting.
  # Example: ALFRESCO_ADMINISTRATORS,SITE_ADMINISTRATORS
  auditreport.allowedGroups=ALL

  # Comma-separated list of site groups that may use the audit report service. 
  # The 'ALL' setting allows all users to use it. The opposite to allow for nobody is an empty setting.
  # Example: SiteManager,SiteCollaborator
  auditreport.allowedSiteGroup=ALL

  # If a user needs to have write permission for the document, to show audit information of a document.
  # Value: false or true
  auditreport.writePermission=false
  ```  
**Notes:**
1. `auditreport.allowedGroups` and `auditreport.writePermission` criteria are `OR` combined. That means one of it must match. 
2. If `auditreport.writePermission` is set to `true` additionally a user need write permission on the document.

A common configuration may:
  ```
  auditreport.allowedGroups=ALFRESCO_ADMINISTRATORS,SITE_ADMINISTRATORS
  auditreport.allowedSiteGroup=SiteManager
  auditreport.writePermission=false
  ```
This ...
1. ... allows Alfresco or Site Administrators to get information of a document.
2. ... allows SiteManager of a site to access audit information of a document. 
3. ... other members of a Site can not access audit information of a document.

# Usage Examples

## Using the Node Audit Trail API

```bash
# Get audit trail for a specific document
curl -X GET "http://localhost:8080/alfresco/service/ev/nodeaudittrail?nodeRef=workspace://SpacesStore/12345678-1234-1234-1234-123456789012" \
  -H "Authorization: Basic <base64-encoded-credentials>"
```

## Using the Permission Check API

```bash
# Check if user has permission to view audit data
curl -X GET "http://localhost:8080/alfresco/service/ev/hasPermissions?nodeRef=workspace://SpacesStore/12345678-1234-1234-1234-123456789012" \
  -H "Authorization: Basic <base64-encoded-credentials>"
```

## Frontend Integration

The addon provides:
- Document Library action button for viewing audit data
- Document Details panel integration
- Custom AIKAU widgets for displaying audit information
- Breadcrumb navigation and back button functionality

# Audit Data Types

The audit trail captures the following types of actions:
- **UPDATE NODE PROPERTIES**: When document properties are modified
- **READ CONTENT**: When document content is accessed
- **COPY**: When documents are copied
- **MOVE**: When documents are moved
- **DELETE**: When documents are deleted
- **CREATE**: When new documents are created

Each audit entry includes:
- User who performed the action
- Timestamp of the action
- Application that generated the audit entry
- Detailed audit values with property changes
- File path information
