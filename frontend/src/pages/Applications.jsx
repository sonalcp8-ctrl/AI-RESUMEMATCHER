import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { applicationApi } from '../services/api';
import { Link } from 'react-router-dom';
import {
  CheckCircle,
  Briefcase,
  MapPin,
  Calendar,
  Clock,
  ExternalLink,
  Layers,
  Building,
} from 'lucide-react';

const Applications = () => {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchApplications = async () => {
      if (!user) return;
      setLoading(true);
      try {
        const res = await applicationApi.getUserApplications(user.id);
        if (res.data?.success) {
          setApplications(res.data.data);
        }
      } catch (err) {
        console.error('Failed to load applications', err);
      } finally {
        setLoading(false);
      }
    };

    fetchApplications();
  }, [user?.id]);

  const getStatusBadge = (status) => {
    switch (status?.toLowerCase()) {
      case 'shortlisted':
        return 'status-shortlisted';
      case 'under review':
        return 'status-review';
      case 'rejected':
        return 'status-rejected';
      default:
        return 'status-applied';
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return 'Recent';
    try {
      const d = new Date(dateStr);
      return d.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
      });
    } catch {
      return dateStr;
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h2>My Job Applications</h2>
          <p>Track the progress and status of all your submitted applications</p>
        </div>
      </div>

      {loading ? (
        <div className="loading-state">
          <div className="spinner"></div>
          <p>Loading your applications...</p>
        </div>
      ) : applications.length === 0 ? (
        <div className="empty-state card">
          <Briefcase size={54} className="empty-icon" />
          <h3>No applications submitted yet</h3>
          <p>Browse our matched job listings and apply with a single click!</p>
          <Link to="/" className="btn-primary mt-3">
            Explore Matched Jobs
          </Link>
        </div>
      ) : (
        <div className="card">
          <div className="applications-table-wrapper">
            <table className="custom-table">
              <thead>
                <tr>
                  <th>Job Title & Company</th>
                  <th>Location</th>
                  <th>Job Type</th>
                  <th>Match Score</th>
                  <th>Status</th>
                  <th>Applied On</th>
                </tr>
              </thead>
              <tbody>
                {applications.map((app) => (
                  <tr key={app.id}>
                    <td>
                      <div className="app-job-info">
                        <span className="app-title">{app.jobTitle}</span>
                        <span className="app-company">
                          <Building size={13} /> {app.company}
                        </span>
                      </div>
                    </td>
                    <td>
                      <span className="meta-text">
                        <MapPin size={14} /> {app.location}
                      </span>
                    </td>
                    <td>
                      <span className="meta-pill text-xs">{app.jobType}</span>
                    </td>
                    <td>
                      <div className="score-cell">
                        <span className="score-val">{app.matchScore}%</span>
                        <div className="score-bar">
                          <div
                            className="score-fill"
                            style={{
                              width: `${app.matchScore}%`,
                              backgroundColor:
                                app.matchScore >= 75
                                  ? '#10b981'
                                  : app.matchScore >= 50
                                  ? '#f59e0b'
                                  : '#64748b',
                            }}
                          ></div>
                        </div>
                      </div>
                    </td>
                    <td>
                      <span className={`status-pill ${getStatusBadge(app.status)}`}>
                        {app.status || 'Applied'}
                      </span>
                    </td>
                    <td>
                      <span className="meta-text">
                        <Calendar size={14} /> {formatDate(app.appliedDate)}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};

export default Applications;
